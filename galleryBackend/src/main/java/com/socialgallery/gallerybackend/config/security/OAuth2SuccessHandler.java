package com.socialgallery.gallerybackend.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialgallery.gallerybackend.advice.exception.BadRequestException;
import com.socialgallery.gallerybackend.config.AppProperties;
import com.socialgallery.gallerybackend.dto.jwt.TokenDTO;
import com.socialgallery.gallerybackend.dto.sign.OAuth2DTO;
import com.socialgallery.gallerybackend.dto.sign.UserRequestMapper;
import com.socialgallery.gallerybackend.entity.security.RefreshToken;
import com.socialgallery.gallerybackend.entity.security.RefreshTokenJpaRepo;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.repository.UserRepository;
import com.socialgallery.gallerybackend.service.security.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;

import static com.socialgallery.gallerybackend.config.security.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private final JwtProvider jwtProvider;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final RefreshTokenJpaRepo refreshTokenJpaRepo;

    private final UserRequestMapper userRequestMapper;

    private final ObjectMapper objectMapper;

    private final AppProperties appProperties;

    private final UserRepository userRepository;

//    @Autowired
//    OAuth2SuccessHandler(JwtProvider jwtProvider, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
//                         RefreshTokenJpaRepo refreshTokenJpaRepo, UserRequestMapper userRequestMapper,
//                         ObjectMapper objectMapper, UserRepository userRepository) {
//        this.jwtProvider = jwtProvider;
//        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
//        this.refreshTokenJpaRepo = refreshTokenJpaRepo;
//        this.userRequestMapper = userRequestMapper;
//        this.objectMapper = objectMapper;
//        this.userRepository = userRepository;
//    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("onAuthenticationSuccessHandler");

        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        OAuth2DTO userDto = userRequestMapper.oAuth2DTO(oAuth2User);


        // 최초 로그인이라면 회원가입 처리를 한다.
        if (userRepository.findByEmail(userDto.getEmail()).isEmpty()) {
            Users users = Users.builder()
                    .email(userDto.getEmail())
                    .username(userDto.getName())
                    .picture(userDto.getPicture())
                    .password("1111")
                    .authProvider("google")
                    .fromSocial(true)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            userRepository.save(users);
        }
        Users users = userRepository.findByEmail(userDto.getEmail()).orElseThrow();
        TokenDTO token = jwtProvider.createTokenDto(users.getId(), users.getRoles());
        log.info("{}", token);

        writeTokenResponse(response, token);

        String path = request.getRequestURI();
        log.info("PATH : " + path);
        RefreshToken refreshToken = RefreshToken.builder()
                .key(users.getId())
                .token(token.getRefreshToken())
                .build();
        refreshTokenJpaRepo.save(refreshToken);

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    private void writeTokenResponse(HttpServletResponse response, TokenDTO token)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader("Authorization", "Bearer " + token.getAccessToken());
        response.setContentType("application/json;charset=UTF-8");

        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String token = jwtProvider.resolveToken(request);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
