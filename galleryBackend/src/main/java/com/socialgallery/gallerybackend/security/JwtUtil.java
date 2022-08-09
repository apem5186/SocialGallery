package com.socialgallery.gallerybackend.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/*
 * @Reference https://goodteacher.tistory.com/98?category=857269
 */

@Component
@Slf4j
public class JwtUtil {

    // application.properties에 사용된 값을 주입받는다.
    @Value("${jwt.salt}")
    private String salt;

    @Value("${jwt.expmin}")
    private Long expiremin;

    public String createAuthToken(String email) {
        return create(email, "authToken", expiremin);
    }

    /*
     * 로그인 성공 시 사용자 정보를 기반으로 JwtToken을 생성해서 반환한다.
     * JWT Token = Header + Payload + Signature
     * @param email
     * @param subject
     * @param expriremin
     * @return
     */

    private String create(String email, String subject, long expiremin) {
        final JwtBuilder builder = Jwts.builder();
        // Header 설정
        // builder.setHeaderParam("typ", "JWT"); // 토큰의 타입으로 고정 값
        // Payload 설정 - claim 정보 포함
        builder.setSubject(subject) // 토큰 제목 설정
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiremin)); // 유효기간
        // 담고 싶은 정보 설정
        if (email != null) {
            builder.claim("user", email);
        }

        // signature -> secretKey를 이용한 암호화
        builder.signWith(SignatureAlgorithm.HS256, salt.getBytes());

        // 마지막 직렬화 처리
        final String jwt = builder.compact();
        log.debug("토큰 발행: {}", jwt);
        return jwt;
    }

    /*
     * Jwt 토큰을 분석해서 필요한 정보를 반환한다.
     * 토큰에 문제가 있다면 Runtime 예외를 발생시킨다.
     * @param jwt
     * @return
     */
    public Map<String, Object> checkAndGetClaims(String jwt) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(jwt);
        log.trace("claims: {}", claims);

        return claims.getBody();
    }
}
