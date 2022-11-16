package com.socialgallery.gallerybackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //모든 경로에 대해
        long MAX_AGE_SECS = 3600;
        registry.addMapping("/**")
            // Origin이 http:localhost:3000에 대해
            .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000", "http://elasticbeanstalk-ap-northeast-2-506714295105.s3-website.ap-northeast-2.amazonaws.com/")
            // GET, POST, PUT, PATCH, DELETE, OPTIONS 메서드를 허용한다.
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("Authorization")
            .allowCredentials(true)
            .maxAge(MAX_AGE_SECS);

    }


}
