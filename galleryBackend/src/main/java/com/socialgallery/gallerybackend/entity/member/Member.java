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
    @GeneratedValue(strategy = GenerationType.AUTO)         // GenerationType : JPA에서 기본키의 생성 전략 타입
    private Long id;                                        // GenerationType.AUTO : GenerationType을 자동으로 설정
    
    @Column(nullable = false)
    private String nickname;
    
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String picture; // 구글이나 네이버 로그인 api 기능 사용시 필요

    private String phone;

    private boolean fromSocial; // 직접 회원가입 했는지, 구글이나 네이버 등으로 회원가입 했는지

    public Member update(String nickname, String picture) {
        this.nickname = nickname;
        this.picture = picture;

        return this;
    }
}
