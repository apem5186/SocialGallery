package com.socialgallery.gallerybackend.security.handler;

import com.socialgallery.gallerybackend.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        // preflight를 위한 OPTIONS 요청은 그냥 전달
        if (req.getMethod().equals("OPTIONS"))
            return true;
        // request의 헤더에서 jwt-auth-token으로 넘어온 녀석을 찾아본다.
        String authToken = req.getHeader("jwt-auth-token");
        log.debug("경로: {}, 토큰: {}", req.getServletPath(), authToken);

        if (authToken != null) {
            // 유효한 토큰이면 진행, 그렇지 않으면 예외를 발생시킨다.
            jwtUtil.checkAndGetClaims(authToken);
            return true;
        } else {
            throw new RuntimeException("인증 토큰이 없습니다.");
        }
    }
}
