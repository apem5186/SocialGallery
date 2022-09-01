//package com.socialgallery.gallerybackend.config.security;
//
//import com.socialgallery.gallerybackend.dto.jwt.TokenDTO;
//import com.socialgallery.gallerybackend.dto.oauth.OAuthAttributes;
//import com.socialgallery.gallerybackend.entity.security.RefreshToken;
//import com.socialgallery.gallerybackend.entity.security.RefreshTokenJpaRepo;
//import com.socialgallery.gallerybackend.entity.user.Users;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Optional;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
//
//    private final PasswordEncoder passwordEncoder;
//
//    private final JwtProvider jwtProvider;
//
//    private final RefreshTokenJpaRepo refreshTokenJpaRepo;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//            throws IOException, ServletException {
//        log.info("onAuthenticationSuccessHandler");
//        String targetUrl = determineTargetUrl(request, response, authentication);
//
//        if (response.isCommitted()) {
//            log.debug("Response has already been committed. Unable to redirect to " +
//                    targetUrl);
//            return;
//        }
//
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
//    }
//
//    // token을 생성하고 이를 포함한 프론트엔드로의 uri를 생성한다.
//    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        Optional<String> redirectUri =
//    }
//}
