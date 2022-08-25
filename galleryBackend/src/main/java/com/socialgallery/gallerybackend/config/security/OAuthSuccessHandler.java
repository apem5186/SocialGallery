//package com.socialgallery.gallerybackend.config.security;
//
//import com.socialgallery.gallerybackend.dto.jwt.TokenDTO;
//import com.socialgallery.gallerybackend.dto.oauth.OAuthAttributes;
//import com.socialgallery.gallerybackend.entity.security.RefreshToken;
//import com.socialgallery.gallerybackend.entity.security.RefreshTokenJpaRepo;
//import com.socialgallery.gallerybackend.entity.user.Users;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Slf4j
//public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
//
//    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//
//    private final PasswordEncoder passwordEncoder;
//
//    private final JwtProvider jwtProvider;
//
//    private final RefreshTokenJpaRepo refreshTokenJpaRepo;
//
//    public OAuthSuccessHandler(PasswordEncoder passwordEncoder, JwtProvider jwtProvider, RefreshTokenJpaRepo refreshTokenJpaRepo){
//        this.passwordEncoder = passwordEncoder;
//        this.jwtProvider = jwtProvider;
//        this.refreshTokenJpaRepo = refreshTokenJpaRepo;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        log.info("onAuthenticationSuccessHandler");
//
//        OAuthAttributes oAuthAttributes = authentication.getPrincipal();
//
//        Users users = oAuthAttributes.toEntity();
//        boolean fromSocial = users.isFromSocial();
//        boolean passwordResult = passwordEncoder.matches("1111", users.getPassword());
//
//        TokenDTO tokenDTO = jwtProvider.createTokenDto(users.getId(), users.getRoles());
//
//        RefreshToken refreshToken = RefreshToken.builder()
//                .token(tokenDTO.getRefreshToken())
//                .key(users.getId())
//                .build();
//
//        refreshTokenJpaRepo.save(refreshToken);
//
//
//        if (fromSocial && passwordResult) {
//            redirectStrategy.sendRedirect(request, response, "/");
//        }
//    }
//}
