package com.socialgallery.gallerybackend.dto.post;

import com.socialgallery.gallerybackend.entity.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDTO {

    private String title;

    private String content;

    private String writer;

    @Builder
    public PostRequestDTO (String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .hits(0)
                .reviewCnt(0)
                .likeCnt(0)
                .build();
    }
}
