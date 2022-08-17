package com.socialgallery.gallerybackend.repository;

import com.socialgallery.gallerybackend.entity.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByPostPid(Long postId);
}
