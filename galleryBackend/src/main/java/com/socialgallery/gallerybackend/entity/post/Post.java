package com.socialgallery.gallerybackend.entity.post;

import com.socialgallery.gallerybackend.entity.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    private String title;

    private String content;

    private String writer;

    private int hits;

    private int reviewCnt;

    private int likeCnt;
}
