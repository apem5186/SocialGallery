package com.socialgallery.gallerybackend.dto.oauth;

import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;

    private String registrationId;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                           String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        return ofGoogle(registrationId, userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name(attributes.get("name").toString())
                .email(attributes.get("email").toString())
                .picture(attributes.get("picture").toString())
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Users toEntity() {
        return Users.builder()
                .username(name)
                .email(email)
                .picture(picture)
                .password("1111")
                .roles(Collections.singletonList("ROLE_USER"))
                .authProvider("GOOGLE")
                .fromSocial(true)
                .build();
    }
}