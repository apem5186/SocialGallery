package com.socialgallery.gallerybackend.repository;

import com.socialgallery.gallerybackend.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
