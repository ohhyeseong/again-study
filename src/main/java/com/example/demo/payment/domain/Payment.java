package com.example.demo.payment.domain;

import com.example.demo.Product.domain.Product;
import com.example.demo.global.entity.BaseEntity;
import com.example.demo.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Payments")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String orderId;

    @Column(nullable = true)
    private String paymentKey;

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public Payment(String orderId, Long amount, PaymentStatus status, User user, Product product){
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.user = user;
        this.product = product;
    }

    public void changePaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
        this.status = PaymentStatus.DONE;
    }
}
