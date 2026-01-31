package com.example.demo.chat.chatmessage.repository;

import com.example.demo.chat.chatmessage.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChatRoomId(Long roomId);
}
