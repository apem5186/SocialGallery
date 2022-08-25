package com.socialgallery.gallerybackend.controller.v1;

import com.socialgallery.gallerybackend.advice.exception.UserNotFoundCException;
import com.socialgallery.gallerybackend.config.security.JwtProvider;
import com.socialgallery.gallerybackend.dto.jwt.TokenDTO;
import com.socialgallery.gallerybackend.entity.security.RefreshToken;
import com.socialgallery.gallerybackend.entity.security.RefreshTokenJpaRepo;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OAuthController {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final RefreshTokenJpaRepo refreshTokenJpaRepo;

    @GetMapping("/oauth2/authorization/google")
    public ResponseEntity<?> setHeader(HttpServletResponse response,
                                       HttpServletRequest request,
                                       OAuth2User oAuth2User) {
        String username = oAuth2User.getAttributes().get("name").toString();

        Users users = userRepository.findByUsername(username).orElseThrow(UserNotFoundCException::new);
        TokenDTO tokenDTO = jwtProvider.createTokenDto(users.getId(), users.getRoles());

        RefreshToken refreshToken = RefreshToken.builder()
                .key(users.getId())
                .token(tokenDTO.getRefreshToken())
                .build();

        refreshTokenJpaRepo.save(refreshToken);

        response.setHeader("Authorization", "Bearer " + tokenDTO.getAccessToken());

        return ResponseEntity.ok().body(tokenDTO);
    }
}
