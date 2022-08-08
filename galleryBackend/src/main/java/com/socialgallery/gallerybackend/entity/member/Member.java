package com.socialgallery.gallerybackend.entity.member;

import com.socialgallery.gallerybackend.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor // 모든 필드값을 파라미터로 받는 생성자를 생성
@NoArgsConstructor  // parameter가 없는 기본 생성자를 생성
@Getter
@ToString
@Table
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)         // GenerationType : JPA에서 기본키의 생성 전략 타입
    private Long id;                                        // GenerationType.AUTO : GenerationType을 자동으로 설정

    @Size(min = 4, max = 255, message = "Minimum username length: 4")
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String picture; // 구글이나 네이버 로그인 api 기능 사용시 필요

    private String phone;

    private boolean fromSocial; // 직접 회원가입 했는지, 구글이나 네이버 등으로 회원가입 했는지

    private String authToken; // jwt 인증 토큰

    private String refreshToken; // authToken 갱신을 위한 토큰

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void addMemberRole(MemberRole memberRole) { roleSet.add(memberRole); }

    public Member update(String username, String picture) {
        this.username = username;
        this.picture = picture;

        return this;
    }
}
