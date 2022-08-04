package com.socialgallery.gallerybackend.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @Description 인증이 되지않은 유저가 요청을 했을때 동작
 * @Reference https://github.com/JangDaeHyeok/Spring-Security/blob/master/spring_security/src/main/java/com/example/demo/security/handler/WebAuthenticationEntryPoint.java
 */
@Component
public class WebAuthenticationEntryPoint implements AuthenticationEntryPoint{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.warn("[WebAuthenticationEntryPoint] 인증되지 않은 사용자 접근");
        response.sendRedirect("error/error403.html");
    }
}