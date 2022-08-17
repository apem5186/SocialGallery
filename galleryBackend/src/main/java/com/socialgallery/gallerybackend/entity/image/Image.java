package com.socialgallery.gallerybackend.entity.image;

import com.socialgallery.gallerybackend.entity.BaseEntity;
import com.socialgallery.gallerybackend.entity.post.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "file")
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long iid;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private String originFileName;  // 파일 원본명

    @Column(nullable = false)
    private String filePath;    // 파일 저장 경로

    private Long fileSize;    // 파일 사이즈

    @Builder
    public Image(String originFileName, String filePath, Long fileSize) {
        this.originFileName = originFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    // post 정보 저장
    public void setPost(Post post) {
        this.post = post;

        // 게시글에 현재 파일이 존재하지 않는다면
        if (!post.getImages().contains(this))
            // 파일 추가
            post.getImages().add(this);
    }


}
