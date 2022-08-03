package com.socialgallery.gallerybackend.repository;

import com.socialgallery.gallerybackend.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);
}
