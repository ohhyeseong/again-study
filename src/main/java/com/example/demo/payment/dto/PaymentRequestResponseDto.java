package com.example.demo.payment.dto;

import com.example.demo.payment.domain.Payment;

public record PaymentRequestResponseDto(
        String orderId,
        String productName,
        Long amount,
        String userEmail,
        String userNickname
) {
    public static PaymentRequestResponseDto from(Payment payment) {
        return new PaymentRequestResponseDto(
                payment.getOrderId(),
                payment.getProduct().getName(), // 연관된 Product에서 가져옴
                payment.getAmount(),
                payment.getUser().getEmail(),
                payment.getUser().getNickname()
        );
    }
}
