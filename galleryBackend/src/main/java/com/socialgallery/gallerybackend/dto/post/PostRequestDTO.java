package com.socialgallery.gallerybackend.dto.post;

import com.socialgallery.gallerybackend.entity.post.Post;
import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDTO {

    private Users users;

    private String title;

    private String content;

    @Builder
    public PostRequestDTO (Users users, String title, String content) {
        this.users = users;
        this.title = title;
        this.content = content;
    }

    public Post toEntity() {
        return Post.builder()
                .users(users)
                .title(title)
                .content(content)
                .hits(0)
                .reviewCnt(0)
                .likeCnt(0)
                .build();
    }
}
