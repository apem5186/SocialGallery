package com.socialgallery.gallerybackend.repository;

import com.socialgallery.gallerybackend.entity.comment.Comment;
import com.socialgallery.gallerybackend.entity.post.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"post"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Comment> findByPost(Post post);

    void deleteByCid(Long cid);
}
