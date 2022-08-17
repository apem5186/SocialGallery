package com.socialgallery.gallerybackend.dto.image;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageDTO {
    private String originFilename;
    private String filePath;
    private Long fileSize;

    @Builder
    public ImageDTO(String originFilename, String filePath, Long fileSize) {
        this.originFilename = originFilename;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
}
