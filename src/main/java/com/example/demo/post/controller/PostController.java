package com.example.demo.post.controller;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.response.ApiResponse;
import com.example.demo.global.security.custom.CustomUserDetails;
import com.example.demo.post.dto.PostCreateDto;
import com.example.demo.post.dto.PostResponse;
import com.example.demo.post.dto.PostUpdateDto;
import com.example.demo.post.service.PostService;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Long>> createPost(@Valid @RequestBody PostCreateDto dto,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        User user = getUser(userDetails);
        Long postId = postService.create(dto,user);
        return ResponseEntity.ok(ApiResponse.ok(postId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getAllPosts(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)Pageable pageable
            ) {
        Page<PostResponse> responsePage = postService.getAll(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.ok(responsePage));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>>getPost(@PathVariable Long postId){
        PostResponse response = postService.getById(postId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>>updatePost(@PathVariable Long postId,
                                                               @Valid @RequestBody PostUpdateDto dto,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails){
        User user = getUser(userDetails);
        Long postId1 = postService.update(postId,dto,user);
        PostResponse response = postService.getById(postId1);
        return ResponseEntity.ok(ApiResponse.ok(response));

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails){

        User user = getUser(userDetails);
        postService.delete(postId,user);
        return ResponseEntity.ok(ApiResponse.ok());
    }



    private User getUser(CustomUserDetails userDetails){
       return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
