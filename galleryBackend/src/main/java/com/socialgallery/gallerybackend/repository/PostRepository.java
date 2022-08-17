package com.socialgallery.gallerybackend.repository;

import com.socialgallery.gallerybackend.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByPidDesc();
}
