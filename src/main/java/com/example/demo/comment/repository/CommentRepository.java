package com.example.demo.comment.repository;

import com.example.demo.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query(value = "SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.post WHERE c.post.id = :postId AND c.parent IS NULL",
            countQuery = "SELECT count(c) FROM Comment c WHERE c.post.id = :postId")
    Page<Comment> findAllByPostIdWithUserAndPost(@Param("postId") Long postId, Pageable pageable);

}
