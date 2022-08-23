package com.socialgallery.gallerybackend.config.security;

import com.socialgallery.gallerybackend.dto.jwt.TokenDTO;
import com.socialgallery.gallerybackend.dto.user.UserRequestDTO;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    public void onAuthenticationSuccess(HttpServletResponse response, HttpServletRequest request, Authentication authentication)
    throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .email(oAuth2User.getAttribute("email"))
                .username(oAuth2User.getAttribute("name"))
                .picture(oAuth2User.getAttribute("picture"))
                .password("non")
                .phone(null)
                .fromSocial(true)
                .build();
        Users users = userRequestDTO.toEntity();
        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다. 가입된 이메일로 로그인을 해주세요.");
        } else {
            userRepository.save(users);
        }

        String targetURL;

        log.info("Principal에서 꺼낸 OAuth2User = {}", oAuth2User);

        TokenDTO tokenDTO = jwtProvider.createTokenDto(users.getId(), users.getRoles());
        log.info("ACCESS TOKEN : " + tokenDTO.getAccessToken());

        targetURL = UriComponentsBuilder.fromUriString("/")
                .queryParam("token", "token")
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetURL);

    }
}
