package com.example.demo.comment.controller;

import com.example.demo.comment.domain.Comment;
import com.example.demo.comment.dto.CommentResponse;
import com.example.demo.comment.dto.CommentCreateRequest;
import com.example.demo.comment.dto.CommentUpdateRequest;
import com.example.demo.comment.service.CommentService;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.response.ApiResponse;
import com.example.demo.global.security.custom.CustomUserDetails;
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
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> getByPostId(@PathVariable Long postId,
                                                                          @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){

        return ResponseEntity.ok(ApiResponse.ok(commentService.getComments(postId, pageable)));
    }

    private User getUser(CustomUserDetails userDetails){
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @PutMapping("{postId}/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails details,
            @Valid @RequestBody CommentUpdateRequest dto)
    {
        User user = getUser(details);

        CommentResponse response = commentService.update(dto, postId, commentId, user);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @DeleteMapping("{postId}/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails details
    ){

        User user = getUser(details);

        commentService.delete(postId,commentId,user);

        return ResponseEntity.ok(ApiResponse.ok());
    }

}
