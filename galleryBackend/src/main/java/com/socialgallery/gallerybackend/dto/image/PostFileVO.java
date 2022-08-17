package com.socialgallery.gallerybackend.dto.image;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostFileVO {
    private String UsersId;
    private String title;
    private String content;
    private List<MultipartFile> files;
}
