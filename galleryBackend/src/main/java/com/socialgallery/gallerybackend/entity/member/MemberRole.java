package com.socialgallery.gallerybackend.entity.member;

import org.springframework.security.core.GrantedAuthority;

public enum MemberRole implements GrantedAuthority {
    ROLE_USER, ROLE_MEMBER, ROLE_ADMIN;

    public String getAuthority() {
        return name();
    }
}
