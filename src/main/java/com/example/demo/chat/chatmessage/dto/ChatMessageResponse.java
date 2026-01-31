package com.example.demo.chat.chatmessage.dto;

import com.example.demo.chat.chatmessage.domain.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long id,
        String sender,
        String message,
        LocalDateTime sentAt
) {
    public static ChatMessageResponse from(ChatMessage chatMessage){
        return new ChatMessageResponse(
                chatMessage.getId(),
                chatMessage.getSender(),
                chatMessage.getMessage(),
                chatMessage.getCreatedAt()
        );
    }
}
