package com.socialgallery.gallerybackend.entity.member;

import com.socialgallery.gallerybackend.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor // 모든 필드값을 파라미터로 받는 생성자를 생성
@NoArgsConstructor  // parameter가 없는 기본 생성자를 생성
@Getter
@ToString
@Table
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nickname;

    private String email;

    private String password;

    private String picture;

    private String phone;

    private boolean fromSocial;

    public Member update(String nickname, String picture) {
        this.nickname = nickname;
        this.picture = picture;

        return this;
    }
}
