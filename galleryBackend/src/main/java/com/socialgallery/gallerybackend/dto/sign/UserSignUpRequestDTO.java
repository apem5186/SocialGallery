package com.socialgallery.gallerybackend.dto.sign;

import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;

@Getter
public class UserSignUpRequestDTO {
    private String email;
    private String password;
    private String username;
    private String phone;

    @Builder
    public UserSignUpRequestDTO(String email, String password, String username, String phone) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phone = phone;
    }

    public Users toEntity() {
        return Users.builder()
                .email(email)
                .password(password)
                .username(username)
                .phone(phone)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }
}
