package com.socialgallery.gallerybackend.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/*
 * @Reference https://ws-pace.tistory.com/87?category=964036
 * Request Header에서 토큰 쿼리값에 해당하는 value값을 통해 토큰을 가져와서 검증을 한다.
 * 검증이 완료되면  토큰에서 유저 정보를 추출해 Anthentication 객체로 반환하고, 해당 정보를 SecurityContextHolder에 저장한다.
 * 이로서 해당 유저는 Authenticate (인증)된다.
 */

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    //request로 들어오는 jwt의 유효성을 검증 - JwtProvider.vaildationToken()을 필터로서 FilterChain에 추가
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        // request에서 token을 취한다.
        String token = jwtProvider.resolveToken((HttpServletRequest) request);

        // 검증
        log.info("[Verifying token]");
        log.info(((HttpServletRequest) request).getRequestURL().toString());

        if (token != null && jwtProvider.validationToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}
