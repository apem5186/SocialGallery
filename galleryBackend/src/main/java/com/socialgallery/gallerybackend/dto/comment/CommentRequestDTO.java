package com.socialgallery.gallerybackend.dto.comment;

import com.socialgallery.gallerybackend.entity.comment.Comment;
import com.socialgallery.gallerybackend.entity.post.Post;
import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CommentRequestDTO {

    private String comment;

    private Users users;

    private Post post;

    @Builder
    public CommentRequestDTO(String comment, Users users, Post post) {
        this.comment = comment;
        this.users = users;
        this.post = post;
    }

    public Comment toEntity() {
        return Comment.builder()
                .users(users)
                .post(post)
                .comment(comment)
                .build();
    }
}
