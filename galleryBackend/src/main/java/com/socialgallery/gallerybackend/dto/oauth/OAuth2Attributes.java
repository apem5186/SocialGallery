package com.socialgallery.gallerybackend.dto.oauth;

import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attributes {
    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String name;
    private String picture;

    public static OAuth2Attributes of(String provider, String attributeKey,
                                      Map<String, Object> attributes) {
        if ("google".equals(provider)) {
            return ofGoogle(attributeKey, attributes);
        }
        throw new RuntimeException();
    }

    private static OAuth2Attributes ofGoogle(String attributeKey,
                                            Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String)attributes.get("picture"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("name", name);
        map.put("email", email);
        map.put("picture", picture);

        return map;
    }
}
