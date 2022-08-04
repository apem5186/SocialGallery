package com.socialgallery.gallerybackend.config;

import com.socialgallery.gallerybackend.security.JwtTokenProvider;
import com.socialgallery.gallerybackend.security.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfigurerAdapter를 확장.
 * JwtTokenProvider를 주입받음.
 * JwtFilter를 통해 Security filterchain에 filter를 추가 등록
 * @Reference https://hou27.tistory.com/entry/Spring-Security-JWT?category=1051556
 */
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}