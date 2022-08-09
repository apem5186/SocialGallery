package com.socialgallery.gallerybackend.config;

import com.socialgallery.gallerybackend.security.JwtTokenProvider;
import com.socialgallery.gallerybackend.security.filter.JwtAuthenticationFilter;
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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    // 인증되지 않은 사용자 접근에 대한 handler
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // authenticationManager를 Bean 등록합니다.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
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
                .csrf().disable()   // csrf 보안 토큰 disable 처리
                .httpBasic().disable()  // 기본 설정 해제
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 해제
                .and()
                .authorizeRequests() // 요청에 대한 사용권한 체크
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/**").permitAll()
                .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/**").permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/**").permitAll()
                .mvcMatchers(HttpMethod.PUT, "/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
                // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
    }


    // Security 무시하기
    public void configure(WebSecurity web)throws Exception{
        web.ignoring().antMatchers("/h2-console/**", "/favicon.ico");
    }

}
