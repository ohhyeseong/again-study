package com.example.demo.chat.chatroom.dto;

import com.example.demo.chat.chatroom.domain.ChatRoom;

public record ChatRoomResponse(
        Long id,
        String name
) {
    public static ChatRoomResponse from(ChatRoom chatRoom){
        return new ChatRoomResponse(chatRoom.getId(), chatRoom.getName());
    }
}
