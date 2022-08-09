package com.socialgallery.gallerybackend.controller;

import com.socialgallery.gallerybackend.dto.UserDTO;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.repository.UserRepository;
import com.socialgallery.gallerybackend.security.JwtTokenProvider;
import com.socialgallery.gallerybackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/index")
    public String index() {
        return "Hello!";
    }

    @ResponseBody
    @PostMapping(value = "api/signIn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signIn(@RequestBody Users users) {
        System.out.println("Sign In");
        System.out.println(users);
        userService.signIn(users.getEmail(), users.getPassword());
        String loginUsers = userService.signIn(users.getEmail(), users.getPassword());
        String result = jwtTokenProvider.getUsername(loginUsers);
            return ResponseEntity.ok()
                    .body(result);
    }

    @ResponseBody
    @PostMapping(value = "api/signUp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signUp(@RequestBody UserDTO dto) {

        System.out.println("Sign Up");
        System.out.println(dto);
        Users registeredUsers = userService.signUp(dto);

        UserDTO userDTO = userService.entitiesToDTO(registeredUsers);

        return ResponseEntity.ok().body(userDTO);
    }

}
