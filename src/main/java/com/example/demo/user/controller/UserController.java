package com.example.demo.user.controller;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.response.ApiResponse;
import com.example.demo.global.security.custom.CustomUserDetails;
import com.example.demo.user.domain.User;
import com.example.demo.user.dto.LoginRequest;
import com.example.demo.user.dto.UserResponse;
import com.example.demo.user.dto.UserSignupDto;
import com.example.demo.user.dto.UserUpdateRequestDto;
import com.example.demo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signup(@Valid @RequestBody UserSignupDto dto){

        UserResponse response = userService.signup(dto);


        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody @Valid LoginRequest dto) {
        // 토큰 발급
        String token = userService.login(dto);

        // 헤더에 토큰 추가
        // HttpHeaders.AUTHORIZATION 상수를 쓰면 오타를 방지할 수 있습니다. (import org.springframework.http.HttpHeaders;)
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(ApiResponse.ok("로그인 성공")); // Body 에는 메시지만
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> myPage(@AuthenticationPrincipal CustomUserDetails userDetails){

        if(userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        UserResponse response = userService.findMyPage(userDetails.getId());

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserResponse>> updateNickname(@AuthenticationPrincipal CustomUserDetails userDetails,@Valid @RequestBody UserUpdateRequestDto dto){

        if(userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        UserResponse response = userService.update(userDetails.getId(), dto);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
