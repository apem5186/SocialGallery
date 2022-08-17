package com.socialgallery.gallerybackend.dto.image;

import com.socialgallery.gallerybackend.entity.image.Image;
import lombok.Getter;

@Getter
public class ImageResponseDTO {
    private Long fileId;    // 파일 id

    public ImageResponseDTO(Image image) {
        this.fileId = image.getIid();
    }
}
