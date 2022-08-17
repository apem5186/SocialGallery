package com.socialgallery.gallerybackend.dto.post;

import com.socialgallery.gallerybackend.entity.post.Post;
import lombok.Getter;

import javax.persistence.Id;
import java.util.List;

@Getter
public class PostResponseDTO {

    private final Long pid;

    private final String title;

    private final String content;

    private final String username;

    private final int hits;

    private final int reviewCnt;

    private final int likeCnt;

    private final List<Long> fileId;  // 첨부 파일 id 목록

    public PostResponseDTO(Post post, List<Long> fileId) {
        this.pid = post.getPid();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUsers().getUsername();
        this.hits = post.getHits();
        this.reviewCnt = post.getReviewCnt();
        this.likeCnt = post.getLikeCnt();
        this.fileId = fileId;
    }


}
