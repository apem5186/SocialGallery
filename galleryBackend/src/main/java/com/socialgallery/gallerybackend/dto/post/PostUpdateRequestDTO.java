package com.socialgallery.gallerybackend.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDTO {

    private String title;
    private String content;

    @Builder
    public PostUpdateRequestDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
