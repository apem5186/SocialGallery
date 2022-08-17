package com.socialgallery.gallerybackend.entity.post;

import com.socialgallery.gallerybackend.entity.BaseEntity;
import com.socialgallery.gallerybackend.entity.image.Image;
import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@ToString
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    @Column(nullable = false)
    private String title;

    private String content;

    private int hits;

    private int reviewCnt;

    private int likeCnt;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Users.class)
    private Users users;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Builder
    public Post(Users users, String title, String content, int hits, int reviewCnt, int likeCnt) {
        this.users = users;
        this.title = title;
        this.content = content;
        this.hits = hits;
        this.reviewCnt = reviewCnt;
        this.likeCnt = likeCnt;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // post에서 파일 처리 위함
    public void addImage(Image image) {
        this.images.add(image);

        // 게시글에 파일이 저장되어 있지 않은 경우
        if (image.getPost() != this)
            // 파일 저장
            image.setPost(this);
    }
}
