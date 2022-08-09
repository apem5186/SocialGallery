package com.socialgallery.gallerybackend;

import com.socialgallery.gallerybackend.security.handler.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@SpringBootApplication
@EnableJpaAuditing  // JPA auditing 기능 사용할 수 있게 함S
public class GalleryBackendApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(GalleryBackendApplication.class, args);
    }

}
