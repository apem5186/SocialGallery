package com.socialgallery.gallerybackend.config;

import com.socialgallery.gallerybackend.config.security.*;
import com.socialgallery.gallerybackend.entity.security.RefreshTokenJpaRepo;
import com.socialgallery.gallerybackend.service.oauth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * @Reference https://ws-pace.tistory.com/87?category=964036
 * Spring Security를 위한 설정, Jwt를 위한 설정, Cors설정 등이 이루어진다.
 */

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    private final JwtProvider jwtProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final RefreshTokenJpaRepo refreshTokenJpaRepo;

    //@Bean
    //public OAuthSuccessHandler oAuthSuccessHandler() { return new OAuthSuccessHandler(passwordEncoder(), jwtProvider, refreshTokenJpaRepo);}
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // authenticationManager를 Bean 등록합니다.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .cors()
                .and()
                .csrf().disable()   // Rest API 이므로 상태를 저장하지 않으니 csrf 보안 토큰 disable 처리
                .httpBasic().disable()  // Rest API 이므로 기본 설정 해제
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 해제
                .and()
                .authorizeRequests() // 요청에 대한 사용권한 체크 antMatchers를 작성하기 위해 먼저 써야함
                .antMatchers("/h2-console/**").permitAll()
                /****
                 * anyRequest.hasRole("USER")와 anyRequest.authenticated() 는 동일한 효과를 낸다.
                 * 로그인, 회원가입 기능 누구나 이용 가능
                 */
                .antMatchers(HttpMethod.POST, "/v1/login", "/v1/signUp").permitAll()
                .antMatchers("/v2/api-docs",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**" ,
                        /*Probably not needed*/ "/swagger.json").permitAll()
                .antMatchers(HttpMethod.GET, "/exception/**").permitAll()

                .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/**").permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/**").permitAll()
                .mvcMatchers(HttpMethod.PUT, "/**").permitAll()
                /*
                 * 그 외는 인증된 회원만 가능
                 */
                .anyRequest().hasRole("USER")

                /*
                 * 로그아웃 설정
                 */
                .and()
                .logout()
                .logoutSuccessUrl("/")


                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)



                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class);
                // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다

        http
                /*
                 * 구글 로그인 설정
                 */
                .oauth2Login()
                .defaultSuccessUrl("http://localhost:8080/")
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .permitAll();


    }


    // Security 무시하기
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/h2-console/**");
    }



}
