package com.example.demo.payment.service;

import com.example.demo.Product.domain.Product;
import com.example.demo.Product.repository.ProductRepository;
import com.example.demo.payment.domain.Payment;
import com.example.demo.payment.domain.PaymentStatus;
import com.example.demo.payment.dto.PaymentConfirmDto;
import com.example.demo.payment.dto.PaymentRequestResponseDto;
import com.example.demo.payment.repository.PaymentRepository;
import com.example.demo.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final RestTemplate restTemplate;

    // 1. 결제 준비 (Redis 저장 포함)
    @Transactional
    public PaymentRequestResponseDto preparePayment(User user, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));

        String orderId = UUID.randomUUID().toString();

        Payment payment = paymentRepository.save(Payment.builder()
                .orderId(orderId)
                .amount(product.getPrice())
                .status(PaymentStatus.READY)
                .user(user)
                .product(product)
                .build());

        // Redis에 검증용 금액 저장 (10분 유효)
        redisTemplate.opsForValue().set(
                "PAYMENT_CHECK:" + orderId,
                product.getPrice().toString(),
                Duration.ofMinutes(10)
        );

        return PaymentRequestResponseDto.from(payment);
    }

    // 2. 결제 승인
    @Transactional
    public Map<String, Object> confirmPayment(PaymentConfirmDto confirmDto) {
        // Redis 검증
        String savedAmount = redisTemplate.opsForValue().get("PAYMENT_CHECK:" + confirmDto.orderId());
        if (savedAmount == null || !savedAmount.equals(confirmDto.amount().toString())) {
            throw new RuntimeException("결제 금액이 위변조되었거나 만료된 요청입니다.");
        }

        // 헤더 설정
        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
        String authorizations = "Basic " + Base64.getEncoder()
                .encodeToString((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizations);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 바디 구성 (Map 사용 권장)
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", confirmDto.orderId());
        params.put("amount", confirmDto.amount());
        params.put("paymentKey", confirmDto.paymentKey());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);

        // 토스 서버로 승인 요청
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.tosspayments.com/v1/payments/confirm",
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                Payment payment = paymentRepository.findByOrderId(confirmDto.orderId())
                        .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));

                payment.changePaymentKey(confirmDto.paymentKey());
                redisTemplate.delete("PAYMENT_CHECK:" + confirmDto.orderId());

                return (Map<String, Object>) response.getBody();
            }
        } catch (Exception e) {
            // 토스 API에서 에러가 발생한 경우 (한도 초과, 잔액 부족 등)
            throw new RuntimeException("토스 결제 승인 중 오류 발생: " + e.getMessage());
        }

        throw new RuntimeException("결제 승인 실패");
    }
}
