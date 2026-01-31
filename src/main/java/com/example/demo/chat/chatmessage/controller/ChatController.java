package com.example.demo.chat.chatmessage.controller;

import com.example.demo.chat.chatmessage.domain.ChatMessage;
import com.example.demo.chat.chatmessage.dto.ChatMessageDto;
import com.example.demo.chat.chatmessage.dto.ChatMessageResponse;
import com.example.demo.chat.chatmessage.service.ChatService;
import com.example.demo.global.response.ApiResponse;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto dto) {
        // 1.DB에 메시지 저장
        chatService.saveMessage(dto);

        // 2. 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + dto.roomId(), dto);
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getAllMessage(@PathVariable Long roomId){

        List<ChatMessageResponse> messages = chatService.getMessage(roomId);

        return ResponseEntity.ok(ApiResponse.ok(messages));
    }
}
