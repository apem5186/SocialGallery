package com.socialgallery.gallerybackend.service;

import com.socialgallery.gallerybackend.dto.MemberDTO;
import com.socialgallery.gallerybackend.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Test
    public void signUpTest() {
        MemberDTO memberDTO = MemberDTO.builder()
                .username("user1")
                .email("user01@social.com")
                .password("1111")
                .build();

        System.out.println(memberService.signUp(memberDTO));
    }

    @Test
    public void getMemberTest() {
        System.out.println(memberService.findMember("user01@social.com"));
    }
}
