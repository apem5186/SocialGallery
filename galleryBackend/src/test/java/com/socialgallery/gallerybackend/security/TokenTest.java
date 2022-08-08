package com.socialgallery.gallerybackend.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class TokenTest {
    @Autowired
    JwtUtil util;

    @Test
    public void tokenGetTest() {
        //given
        String email = "member@social.com";
        //when
        String token = util.createAuthToken(email);
        //then
        assertNotNull(token);
        log.debug(token);
    }

    @Test
    public void contentTest() {
        //given
        String email = "member@social.com";
        //when
        String token = util.createAuthToken(email);
        Map<String, Object> content = util.checkAndGetClaims(token);
        //then
        assertEquals(content.get("sub"), "authToken");
        assertEquals(content.get("user"), email);
    }

    @Test
    public void wrongTokenTest() {
        //given
        String fakeToken = "fakeToken";
        //when-then
        assertThrows(MalformedJwtException.class, () -> {
            util.checkAndGetClaims(fakeToken);
        });
    }

    @Test
    public void expiredTest() {
        //given
        String fakeToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhdXRoVG9rZW4iLCJleHAiOjE2NTk3MTA1NTAsInVzZXIiOiJtZW1iZXJAc29jaWFsLmNvbSJ9.dn6GkwYjGYWh0lVzIpVmcalwhQ8KUZwbB5F2JTHeRVk";
        //when-then
        assertThrows(ExpiredJwtException.class, () -> {
            util.checkAndGetClaims(fakeToken);
        });

    }
}
