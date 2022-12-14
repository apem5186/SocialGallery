package com.socialgallery.gallerybackend.repository;

import com.socialgallery.gallerybackend.entity.comment.Comment;
import com.socialgallery.gallerybackend.entity.post.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"post"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Comment> findByPost(Post post);

    @Modifying
    @Query("DELETE FROM Comment WHERE 'users_id' =:id")
    void deleteById(@Param("id") String id);

    @Query("SELECT co FROM Comment co WHERE co.users.id =:id")
    List<Comment> findAllByUid(@Param("id") Long id);

    @Modifying
    @Query("DELETE FROM Comment co WHERE co.cid =:cid")
    void deleteByCid(@Param("cid") Long cid);
}
