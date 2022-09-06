package com.socialgallery.gallerybackend.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final RefreshTokenJpaRepo refreshTokenJpaRepo;

    private final UserRequestMapper userRequestMapper;

    private final ObjectMapper objectMapper;

    private final SignService signService;

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("onAuthenticationSuccessHandler");
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

        RefreshToken refreshToken = RefreshToken.builder()
                .key(users.getId())
                .token(token.getRefreshToken())
                .build();
        refreshTokenJpaRepo.save(refreshToken);

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
}
