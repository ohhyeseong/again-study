package com.example.demo.post.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.post.domain.Post;
import com.example.demo.post.dto.PostCreateDto;
import com.example.demo.post.dto.PostResponse;
import com.example.demo.post.repository.PostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long create(PostCreateDto dto, User user) throws IOException {
        Post post = Post.builder()
                .title(dto.title())
                .content(dto.content())
                .user(user)
                .build();

        return postRepository.save(post).getId();
    }

    public PostResponse getById(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return PostResponse.from(post);
    }

    public Page<PostResponse> getAll(String keyword,Pageable pageable){
        Page<Post>
    }
}
