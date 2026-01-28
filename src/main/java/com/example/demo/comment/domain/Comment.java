package com.example.demo.comment.domain;

import com.example.demo.global.entity.BaseEntity;
import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 자기 참조(대댓글 기능)
    @ManyToOne(fetch = FetchType.LAZY) // 내 부모는 누구인가?(한 명)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "parent", orphanRemoval = true) // 내 자식들은 누구인가? (여러 명)
    private List<Comment> children = new ArrayList<>();

    @Builder
    public Comment(String content, Post post, User user, Comment parent){
        this.content = content;
        this.post = post;
        this.user = user;
        this.parent = parent;
    }

    public void update(String content) {
        this.content = content;
    }
}
