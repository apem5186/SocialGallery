package com.socialgallery.gallerybackend.service;

import com.socialgallery.gallerybackend.dto.UserDTO;
import com.socialgallery.gallerybackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UsersServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void signUpTest() {
        UserDTO userDTO = UserDTO.builder()
                .username("user1")
                .email("user01@social.com")
                .password("1111")
                .build();

        System.out.println(userService.signUp(userDTO));
    }

    @Test
    public void getUserTest() {
        System.out.println(userService.findUser("user01@social.com"));
    }
}
