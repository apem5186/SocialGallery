package com.socialgallery.gallerybackend.controller;

import com.socialgallery.gallerybackend.dto.MemberDTO;
import com.socialgallery.gallerybackend.entity.member.Member;
import com.socialgallery.gallerybackend.security.JwtUtil;
import com.socialgallery.gallerybackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public String index() {
        return "Hello!";
    }

    @ResponseBody
    @PostMapping(value = "user/signIn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> signIn(@RequestBody Member member,
        HttpServletRequest req, HttpServletResponse res) {

        System.out.println("Sign In");
        System.out.println(member);

        Map<String, Object> resultMap = new HashMap<>();

        Member loginMember = memberService.signIn(member.getEmail(), member.getPassword());
            log.info("loginMember authToken : " + loginMember.getAuthToken());
            // 생성된 토큰 정보를 클라이언트에게 전달한다.
            resultMap.put("jwt-auth-token", loginMember.getAuthToken());

            Map<String, Object> info =
                    jwtUtil.checkAndGetClaims(loginMember.getAuthToken());
            resultMap.putAll(info);

            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    @ResponseBody
    @PostMapping(value = "user/signUp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signUp(@RequestBody MemberDTO dto) {

        System.out.println("Sign Up");
        System.out.println(dto);
        Member registeredMember = memberService.signUp(dto);

        MemberDTO memberDTO = memberService.entitiesToDTO(registeredMember);

        return ResponseEntity.ok().body(memberDTO);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo(HttpServletRequest req) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("info", memberService.getServerInfo());
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

}
