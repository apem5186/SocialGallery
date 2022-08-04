package com.socialgallery.gallerybackend.controller;

import com.socialgallery.gallerybackend.dto.MemberDTO;
import com.socialgallery.gallerybackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
public class SignUpController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    @ResponseBody
    public MemberDTO signUp(@RequestBody MemberDTO dto) {

        System.out.println("signUp");
        System.out.println(dto.getEmail());
        System.out.println(dto.getUsername());
        System.out.println(dto.getPassword());
        memberService.signUp(dto);
        MemberDTO result = memberService.findMember(dto.getEmail());

        return result;
    }
}
