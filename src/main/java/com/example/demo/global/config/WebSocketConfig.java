package com.example.demo.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 연결할 주소: ws: //localhost:8081/ws-stomp
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")// 모든 도메인 허용 (테스트용)
                .withSockJS(); // SockJs 지원
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지 받을 때 (구독): /sub/chat/room/1
        registry.enableSimpleBroker("/sub");

        // 메시지 보낼 때 (발행): /pub/chat/message
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
