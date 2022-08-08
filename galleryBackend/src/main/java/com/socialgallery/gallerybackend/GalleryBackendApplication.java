package com.socialgallery.gallerybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // JPA auditing 기능 사용할 수 있게 함
public class GalleryBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GalleryBackendApplication.class, args);
    }

}
