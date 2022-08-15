package com.socialgallery.gallerybackend.dto.sign;

import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignUpRequestDTO {
    private String email;
    private String password;
    private String username;
    private String phone;
    private String provider = null;

    public Users toEntity(PasswordEncoder passwordEncoder) {
        return Users.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .username(username)
                .phone(phone)
                .provider(provider)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }

    public Users toEntity() {
        return Users.builder()
                .email(email)
                .username(username)
                .provider(provider)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

    }
}
