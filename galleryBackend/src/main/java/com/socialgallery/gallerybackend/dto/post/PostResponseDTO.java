package com.socialgallery.gallerybackend.dto.post;

import com.socialgallery.gallerybackend.entity.post.Post;
import lombok.Getter;

import javax.persistence.Id;

@Getter
public class PostResponseDTO {

    private final Long pid;

    private final String title;

    private final String content;

    private final String writer;

    private final int hits;

    private final int reviewCnt;

    private final int likeCnt;

    public PostResponseDTO(Post post) {
        this.pid = post.getPid();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getWriter();
        this.hits = post.getHits();
        this.reviewCnt = post.getReviewCnt();
        this.likeCnt = post.getLikeCnt();
    }


}
