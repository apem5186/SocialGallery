package com.socialgallery.gallerybackend.dto.sign;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OAuth2DTO {
    private String email;
    private String name;
    private String picture;

    @Builder
    public OAuth2DTO(String email, String name, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
    }
}
