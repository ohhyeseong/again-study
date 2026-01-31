package com.example.demo.chat.chatroom.service;

import com.example.demo.chat.chatroom.domain.ChatRoom;
import com.example.demo.chat.chatroom.dto.ChatRoomCreateRequest;
import com.example.demo.chat.chatroom.dto.ChatRoomResponse;
import com.example.demo.chat.chatroom.repository.ChatRoomRepository;
import com.example.demo.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoomResponse createChatRoom(String name, User user){
        ChatRoom chatRoom = ChatRoom.builder()
                .name(name)
                .user(user)
                .build();

        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);

        return ChatRoomResponse.from(savedRoom);
    }

    public List<ChatRoomResponse> findAllRoom() {
        return chatRoomRepository.findAll().stream()
                .map(ChatRoomResponse::from)
                .toList();
    }
}
