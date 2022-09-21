package com.socialgallery.gallerybackend.controller.aws;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @GetMapping("/")
    public String healthCheck() {
        return "The Service is up and running...";
    }
}
