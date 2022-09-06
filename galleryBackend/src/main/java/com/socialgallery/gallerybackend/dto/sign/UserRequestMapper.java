package com.socialgallery.gallerybackend.dto.sign;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {

    public OAuth2DTO oAuth2DTO(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();

        return OAuth2DTO.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .picture((String) attributes.get("picture"))
                .build();
    }

}
