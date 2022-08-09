package com.socialgallery.gallerybackend.config;

import com.socialgallery.gallerybackend.security.JwtTokenProvider;
import com.socialgallery.gallerybackend.security.filter.JwtTokenFilter;
import com.socialgallery.gallerybackend.security.handler.JwtAccessDeniedHandler;
import com.socialgallery.gallerybackend.security.handler.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    // 인증되지 않은 사용자 접근에 대한 handler
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
//        http
//                .authorizeRequests()
//                .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers("/","/**").access("permitAll")
//                .antMatchers("/h2-console/**").permitAll() // 추가
//                .and()
//                .csrf() // 추가
//                .and()
//                .csrf().disable()
//                .ignoringAntMatchers("/h2-console/**").disable() // 추가
//                .httpBasic();
        http.cors()
                .and()
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/", "/user/**").permitAll()
                .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/**").permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/**").permitAll()
                .mvcMatchers(HttpMethod.PUT, "/**").permitAll()
                .anyRequest()
                .authenticated();
    }


    // Security 무시하기
    public void configure(WebSecurity web)throws Exception{
        web.ignoring().antMatchers("/h2-console/**");
    }

}
