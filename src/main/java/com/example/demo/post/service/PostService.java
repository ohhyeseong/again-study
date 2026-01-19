package com.example.demo.post.service;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.post.domain.Post;
import com.example.demo.post.dto.PostCreateDto;
import com.example.demo.post.dto.PostResponse;
import com.example.demo.post.dto.PostUpdateDto;
import com.example.demo.post.repository.PostRepository;
import com.example.demo.user.domain.User;
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
        Page<Post> posts;
        if(keyword == null || keyword.isBlank()) {
            posts = postRepository.findAll(pageable);
        } else {
            posts = postRepository.findByTitleContaining(keyword,pageable);
        }
        return posts.map(PostResponse::from);
    }

    @Transactional
    public Long update(Long postId, PostUpdateDto dto, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.VALIDATION_ERROR);
        }

        post.update(dto.title(), dto.content());
        return post.getId();
    }

    @Transactional
    public void delete(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if(!post.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.VALIDATION_ERROR);
        }
        postRepository.delete(post);
    }

}
