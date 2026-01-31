package com.example.demo.chat.chatmessage.service;

import com.example.demo.chat.chatmessage.domain.ChatMessage;
import com.example.demo.chat.chatmessage.dto.ChatMessageDto;
import com.example.demo.chat.chatmessage.dto.ChatMessageResponse;
import com.example.demo.chat.chatmessage.repository.ChatMessageRepository;
import com.example.demo.chat.chatroom.domain.ChatRoom;
import com.example.demo.chat.chatroom.repository.ChatRoomRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void saveMessage(ChatMessageDto dto){
        // 1. 방 찾기 (없으면 예외 처리)
        ChatRoom chatRoom = chatRoomRepository.findById(dto.roomId())
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
        
        // 2. 메시지 엔티티 생성
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(dto.sender())
                .message(dto.message())
                .build();
        
        // 3. 저장
        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageResponse> getMessage(Long roomId){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        return chatMessageRepository.findAllByChatRoomId(roomId).stream()
                .map(ChatMessageResponse::from)
                .toList();
    }
}
