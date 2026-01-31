package com.example.demo.chat.chatroom.controller;

import com.example.demo.chat.chatroom.dto.ChatRoomCreateRequest;
import com.example.demo.chat.chatroom.dto.ChatRoomResponse;
import com.example.demo.chat.chatroom.service.ChatRoomService;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.response.ApiResponse;
import com.example.demo.global.security.custom.CustomUserDetails;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ChatRoomResponse>> createdRoom(
            @RequestBody @Valid ChatRoomCreateRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = getUser(userDetails);
        ChatRoomResponse response = chatRoomService.createChatRoom(dto.name(),user);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // 채팅방 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<ChatRoomResponse>>> getAllRooms() {
        List<ChatRoomResponse> rooms = chatRoomService.findAllRoom();
        return ResponseEntity.ok(ApiResponse.ok(rooms));
    }

    private User getUser(CustomUserDetails userDetails){
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
