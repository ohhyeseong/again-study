package com.example.demo.comment.controller;

import com.example.demo.comment.dto.CommentResponse;
import com.example.demo.comment.repository.CommentCreateRequest;
import com.example.demo.comment.service.CommentService;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.response.ApiResponse;
import com.example.demo.global.security.custom.CustomUserDetails;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;


    @PostMapping("/create/{postId}")
    public ResponseEntity<ApiResponse<CommentResponse>> create(@Valid @RequestBody CommentCreateRequest dto,
                                                               @PathVariable Long postId,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        User user = getUser(userDetails);

        CommentResponse response = commentService.create(dto, user, postId);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("{postId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getByPostId(@PathVariable Long postId){

        return ResponseEntity.ok(ApiResponse.ok(commentService.getComments(postId)));
    }

    private User getUser(CustomUserDetails userDetails){
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
