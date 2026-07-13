package com.example.chating.controller;

import com.example.chating.dto.request.ChatMessageSendRequest;
import com.example.chating.dto.response.ChatMessageHistorySearchResponse;
import com.example.chating.global.dto.ApiResponse;
import com.example.chating.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{chat-room-id}")
    public void sendMessage(
            Principal principal,
            @DestinationVariable("chat-room-id") Long chatRoomId,
            @Payload ChatMessageSendRequest request
    ) {
        ChatMessageHistorySearchResponse response =
                chatService.sendMessage(principal.getName(), chatRoomId, request);

        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, response);
    }

    @MessageExceptionHandler
    public void handleMessageException(Principal principal, Exception e) {
        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/errors",
                ApiResponse.fail("[ERROR: Chat/Message] " + e.getMessage())
        );
    }
}
