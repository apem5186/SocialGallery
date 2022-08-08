package com.socialgallery.gallerybackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialgallery.gallerybackend.dto.MemberDTO;
import com.socialgallery.gallerybackend.entity.member.Member;
import com.socialgallery.gallerybackend.entity.member.MemberRole;
import com.socialgallery.gallerybackend.repository.MemberRepository;
import com.socialgallery.gallerybackend.security.JwtUtil;
import com.socialgallery.gallerybackend.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWithIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class MemberControllerTest {

    @Autowired
    MockMvc mock;

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtUtil util;

    @Test
    @DisplayName("SignUp 테스트")
    public void testSignUp() throws Exception {
        Member member = Member.builder()
                .username("user01")
                .email("user01@social.com")
                .password(passwordEncoder.encode("1111"))
                .fromSocial(false)
                .build();
        member.addMemberRole(MemberRole.ROLE_USER);


        MemberDTO memberDTO = memberService.entitiesToDTO(member);

        memberService.signUp(memberDTO);



    }

    @Test
    @DisplayName("정보를 전달해서 로그인 후 토큰을 잘 얻을 수 있는지 확인한다.")
    public void testLogin() throws Exception {
        // given
        Map<String, String> map = new HashMap<>();
        map.put("email", "user01@social.com");
        map.put("password", "1111");
        String content = new ObjectMapper().writeValueAsString(map);

        // when
        MockHttpServletRequestBuilder reqBuilder
                = post("/user/signIn").contentType("application/json").content(content);
        ResultActions action = mock.perform(reqBuilder);

        // then
        action.andExpect(status().is(202))
                .andExpect(jsonPath("$.sub", equalTo("authToken")))
                .andExpect(jsonPath("$.user", equalTo(map.get("email"))));

    }

    @Test
    @DisplayName("정상적인 토큰을 전달했을 때 원하는 정보를 잘 얻을 수 있는지 확인한다.")
    public void testGetInfoSuccess() throws Exception {
        // given
        String token = util.createAuthToken("member@quietjun.xyz");

        // when
        MockHttpServletRequestBuilder reqBuilder
                = get("/api/info").header("jwt-auth-token", token);
        ResultActions action = mock.perform(reqBuilder);

        // then
        action.andExpect(status().is(202))
                .andExpect(jsonPath("$.info", startsWithIgnoringCase("여기는 은서파, 지금 시각은 ")));
    }

    @Test
    @DisplayName("비 정상적인 토큰을 전달했을 때 예외가 발생하는지 확인한다.")
    public void testGetInfoFail() throws Exception {
        // given
        String token = "malfomed token";

        // when
        MockHttpServletRequestBuilder reqBuilder
                = get("/api/info").header("jwt-auth-token", token);

        // then
        assertThrows(Exception.class, () -> {
            mock.perform(reqBuilder);
        });
    }
}
