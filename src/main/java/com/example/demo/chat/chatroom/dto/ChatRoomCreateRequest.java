package com.example.demo.chat.chatroom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record ChatRoomCreateRequest(
        @NotBlank(message = "필수 입력란입니다!")
        @Size(min = 0, max = 20, message = "0 ~ 20자 까지 입력해주세요.")
        String name
) {
}
