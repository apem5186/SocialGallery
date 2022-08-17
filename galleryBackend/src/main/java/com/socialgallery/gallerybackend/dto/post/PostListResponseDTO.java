package com.socialgallery.gallerybackend.dto.post;

import com.socialgallery.gallerybackend.entity.post.Post;
import lombok.Getter;

@Getter
public class PostListResponseDTO {

    private Long pid;
    private String username;
    private String title;
    private String content;

    private Long thumbnailId;   // 썸네일 Id

    public PostListResponseDTO(Post post) {
        this.pid = post.getPid();
        this.username = post.getUsers().getUsername();
        this.title = post.getTitle();
        this.content = post.getContent();

        if (!post.getImages().isEmpty())    // 첨부파일 존재 o
            this.thumbnailId = post.getImages().get(0).getIid();
        else // 첨부파일 존재 x
            this.thumbnailId = 0L;  // 서버에 저장된 기본 이미지 반환
    }
}
