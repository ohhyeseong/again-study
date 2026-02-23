package com.example.demo.payment.dto;

public record PaymentConfirmDto(
        String paymentKey,
        String orderId,
        Long amount
) {
}
