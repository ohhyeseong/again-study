package com.example.demo.user.controller;

import com.example.demo.global.response.ApiResponse;
import com.example.demo.user.dto.UserSignupDto;
import com.example.demo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Long>> signup(@Valid @RequestBody UserSignupDto dto){

        Long userId = userService.signup(dto);

        return ResponseEntity.ok(ApiResponse.ok(userId));
    }
}
