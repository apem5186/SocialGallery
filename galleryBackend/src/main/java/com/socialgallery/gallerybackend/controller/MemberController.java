package com.socialgallery.gallerybackend.controller;

import com.socialgallery.gallerybackend.dto.MemberDTO;
import com.socialgallery.gallerybackend.entity.member.Member;
import com.socialgallery.gallerybackend.security.JwtUtil;
import com.socialgallery.gallerybackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberController {

    private final MemberService memberService;

    private final JwtUtil jwtUtil;

    @GetMapping("/index")
    public void index() {
    }

    @PostMapping("user/signIn")
    public ResponseEntity<Map<String, Object>> signIn(@RequestBody Member member,
        HttpServletRequest req, HttpServletResponse res) {

        Map<String, Object> resultMap = new HashMap<>();

        Member loginMember = memberService.signIn(member.getEmail(), member.getPassword());
            // 생성된 토큰 정보를 클라이언트에게 전달한다.
            resultMap.put("jwt-auth-token", loginMember.getAuthToken());

            Map<String, Object> info =
                    jwtUtil.checkAndGetClaims(loginMember.getAuthToken());
            resultMap.putAll(info);

            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    @PostMapping("user/signUp")
    @ResponseBody
    public MemberDTO signUp(@RequestBody MemberDTO dto) {

        System.out.println("Sign Up");
        System.out.println(dto);
        memberService.signUp(dto);
        MemberDTO result = memberService.findMember(dto.getEmail());

        return result;
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo(HttpServletRequest req) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("info", memberService.getServerInfo());
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

}
