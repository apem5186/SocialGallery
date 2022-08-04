package com.socialgallery.gallerybackend.security.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * AuthenticationEntryPoint와 달리 AccessDeniedHandler는
 * 유저 정보는 있으나, 엑세스 권한이 없는 경우 동작하는 친구이다.
 * @Reference https://github.com/hou27/spring-boot-jwt-example/blob/jwt/src/main/java/demo/api/jwt/JwtAccessDeniedHandler.java
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
