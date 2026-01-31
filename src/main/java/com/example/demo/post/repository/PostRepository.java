package com.example.demo.post.repository;

import com.example.demo.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user WHERE p.title LIKE %:keyword%",
            countQuery = "SELECT COUNT(p) FROM Post p WHERE p.title LIKE %:keyword%")
    Page<Post> findByTitleContaining(@Param("keyword")String keyword, Pageable pageable);


    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user",
            countQuery = "SELECT COUNT(p) FROM Post p")
    Page<Post> findAllWithUser(Pageable pageable);
}
