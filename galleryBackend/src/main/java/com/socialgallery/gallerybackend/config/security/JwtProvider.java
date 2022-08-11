package com.socialgallery.gallerybackend.config.security;

import com.socialgallery.gallerybackend.advice.exception.AuthenticationEntryPointCException;
import com.socialgallery.gallerybackend.dto.jwt.TokenDTO;
import com.socialgallery.gallerybackend.service.security.CustomUserDetailService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/*
 * @Reference https://ws-pace.tistory.com/87?category=964036
 * 유저 정보로 Jwt를 발급하거나 Jwt로 유저 정보를 가져온다.
 * JwtProvider에서 Jwt 생성, 유저 정보 매핑, Jwt 암호화, 복호화, 검증이 이뤄진다.
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("spring.jwt.secret")
    private String secretKey;
    private String ROLES = "roles";

    private final Long accessTokenValidMillisecond = 60 * 60 * 1000L;   // 1hour
    private final Long refreshTokenValidMillisecond = 14 * 24 * 60 * 60 * 1000L;    // 14days

    private final CustomUserDetailService userDetailService;

    //Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /******
     * jwt 생성
     * 토큰에 저장할 유저 pk와 권한 리스트를 매개변수로 받는다.
     * pk는 setSubject로 저장하고, roles들은 key-value 형태로 넣어준다. ("roles" : {"권한1", "권한2", ...})
     * access, refresh토큰을 각각 만들어서 tokenDto로 만든 후 반환.
     */
    public TokenDTO createTokenDto(Long id, List<String> roles) {

        // Claims 에 user 구분을 위한 User pk 및 authorities 목록 삽입
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put(ROLES, roles);
        // 생성날짜, 만료날짜를 위한 Date
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenDTO.builder()
                .grantType("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(accessTokenValidMillisecond)
                .build();
    }

    /****
     * jwt로 인증정보를 조회
     * Jwt에서 권한정보를 확인하기 위해 시크릿키으로 검증 후 권한 목록을 가져온다 (만약 키에 문제가 있다면 SignatureException이 발생한다
     * claims를 토큰에서 빼온 후 권한이 있는지 확인하고 있다면 pk값을 가지고 loadUserByUsername()을 통해 유저 엔티티를 받는다.
     * User 엔티티가 UserDetails를 상속받아서 getAuthorize()를 재정의하였으므로 사용하면 된다.
     */

    public Authentication getAuthentication(String token) {
        // Jwt 에서 claims 추출
        Claims claims = parseClaims(token);
        log.info("CLAIMS : " + claims);

        // 권한 정보가 없음
        if (claims.get(ROLES) == null) {
            throw new AuthenticationEntryPointCException();
        }

        UserDetails userDetails = userDetailService.loadUserByUsername(claims.getSubject());
        log.info("GET AUTHENTICATION FROM JWTPROVIDER : " + userDetails);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰 복호화해서 가져오기
    // 만료된 토큰이여도 refresh token을 검증 후 재발급할 수 있도록 claims를 반환해 준다.
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // jwt에서 회원 구분 id 추출
    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // HTTP Request의 Header에서 Token Parsing -> "X-AUTH-TOKEN: jwt"
    // Request Header에 "X-AUTH-TOKEN" 이 있으면 탈취해서 Jwt값으로 취한다.
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // jwt의 유효성 및 만료일자 확인
    // Jwts에서 제공하는 예외처리를 이용한다.
    public boolean validationToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error(e.toString());
            return false;
        }
    }
}
