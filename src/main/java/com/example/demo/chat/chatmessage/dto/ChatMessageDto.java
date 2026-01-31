package com.example.demo.chat.chatmessage.dto;

public record ChatMessageDto(
        Long roomId,
        String sender,
        String message
) {
}
