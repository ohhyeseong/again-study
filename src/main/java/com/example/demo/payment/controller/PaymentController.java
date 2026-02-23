package com.example.demo.payment.controller;

import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.security.custom.CustomUserDetails;
import com.example.demo.payment.dto.PaymentConfirmDto;
import com.example.demo.payment.dto.PaymentRequestResponseDto;
import com.example.demo.payment.service.PaymentService;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserRepository userRepository; // 추가

    /**
     * 1. 결제 준비 API
     */
    @PostMapping("/prepare")
    public ResponseEntity<PaymentRequestResponseDto> preparePayment(
            @RequestParam Long productId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // 1. 유저 ID 결정 (인증 정보 없으면 테스트용 1L 사용)
        Long userId = (userDetails != null) ? userDetails.getId() : 1L;

        // 2. UserRepository를 사용하여 실제 User 객체 조회
        // (상단에 @RequiredArgsConstructor와 private final UserRepository userRepository; 가 있어야 함)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        // 3. 이제 Long이 아닌 User 객체를 전달!
        PaymentRequestResponseDto response = paymentService.preparePayment(user, productId);

        return ResponseEntity.ok(response);
    }

    /**
     * 2. 결제 승인 API
     */
    @PostMapping("/confirm")
    public ResponseEntity<Map<String, Object>> confirmPayment(@RequestBody PaymentConfirmDto confirmDto) {
        Map<String, Object> response = paymentService.confirmPayment(confirmDto);
        return ResponseEntity.ok(response);
    }

    // 채팅 컨트롤러와 동일한 유저 조회 로직
    private User getUser(CustomUserDetails userDetails) {
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}