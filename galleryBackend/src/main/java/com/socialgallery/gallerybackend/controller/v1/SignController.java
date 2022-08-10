package com.socialgallery.gallerybackend.controller.v1;

import com.socialgallery.gallerybackend.config.security.JwtProvider;
import com.socialgallery.gallerybackend.dto.sign.UserLoginResponseDTO;
import com.socialgallery.gallerybackend.dto.sign.UserSignUpRequestDTO;
import com.socialgallery.gallerybackend.model.response.SingleResult;
import com.socialgallery.gallerybackend.service.ResponseService;
import com.socialgallery.gallerybackend.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@Api(tags = "1. SignUp / Login")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class SignController {

    private final UsersService usersService;
    private final JwtProvider jwtProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "이메일로 로그인을 합니다.")
    @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<String> login(
            @ApiParam(value = "로그인 아이디 : 이메일", required = true) @RequestParam String email,
            @ApiParam(value = "로그인 비밀번호", required = true) @RequestParam String password) {
        UserLoginResponseDTO userLoginDTO = usersService.login(email, password);

        String token = jwtProvider.createToken(String.valueOf(userLoginDTO.getId()), userLoginDTO.getRoles());

        return responseService.getSingleResult(token);
    }

    @ApiOperation(value = "회원가입", notes = "회원가입을 합니다.")
    @PostMapping(value = "/signUp", produces = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> signUp(
            @ApiParam(value = "회원 가입 아이디 : 이메일", required = true) @RequestParam String email,
            @ApiParam(value = "회원 가입 비밀번호", required = true) @RequestParam String password,
            @ApiParam(value = "회원 가입 유저네임", required = true) @RequestParam String username,
            @ApiParam(value = "회원 가입 핸드폰번호", required = false) @RequestParam String phone) {

        UserSignUpRequestDTO userSignUpRequestDTO = UserSignUpRequestDTO.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .username(username)
                .phone(phone)
                .build();
        Long signUpId = usersService.signUp(userSignUpRequestDTO);
        return responseService.getSingleResult(signUpId);
    }
}
