package com.socialgallery.gallerybackend.controller;

import com.socialgallery.gallerybackend.dto.MemberDTO;
import com.socialgallery.gallerybackend.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberControllerTest {

    @Autowired
    private MemberService memberService;


    @Test
    public void insertUser() {
        MemberDTO memberDTO = MemberDTO.builder()
                .nickname("user01")
                .email("user01@social.com")
                .password("1111")
                .build();

        memberService.signUp(memberDTO);
    }
}
