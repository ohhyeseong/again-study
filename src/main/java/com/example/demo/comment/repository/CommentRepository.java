package com.example.demo.comment.repository;

import com.example.demo.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
// 여기 쿼리문에서 n+1을 해결하기 위해 join fetch를 사용해서 조회할때 한꺼번에 가져와 저장시키는데
    // 이렇게 되면 대댓글 의 부모도 다시 다 가져오기 때문에 c.parent IS NULL 이걸 추가해줘서 방지시켜준다.
    @Query(value = "SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.post WHERE c.post.id = :postId AND c.parent IS NULL",
            countQuery = "SELECT count(c) FROM Comment c WHERE c.post.id = :postId")
    Page<Comment> findAllByPostIdWithUserAndPost(@Param("postId") Long postId, Pageable pageable);

}
