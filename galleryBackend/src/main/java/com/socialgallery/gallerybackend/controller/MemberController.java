package com.socialgallery.gallerybackend.controller;

import com.socialgallery.gallerybackend.dto.MemberDTO;
import com.socialgallery.gallerybackend.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RestController
@Log4j2
@RequestMapping("/")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/signUp")
    public void signUpGet() {
        log.info("signIn get...");
    }

    @PostMapping("/signUp")
    public String signUp(MemberDTO dto, RedirectAttributes redirectAttributes) {

        log.info("memberDTO... " + dto);

        Long id = memberService.signUp(dto);

        log.info("memberId... = " + id);

        redirectAttributes.addFlashAttribute("msg", id);

        return "redirect:/index";

    }
}
