package com.socialgallery.gallerybackend.repository;

import com.socialgallery.gallerybackend.entity.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = {"roleSet"})
    Member findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Member findByUsername(String username);
}
