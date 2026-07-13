package com.example.chating.controller;

import com.example.chating.dto.request.ChatRoomCreateRequest;
import com.example.chating.dto.response.ChatMessageHistorySearchResponses;
import com.example.chating.dto.response.ChatRoomCreateResponse;
import com.example.chating.dto.response.ChatRoomListSearchResponses;
import com.example.chating.global.dto.ApiResponse;
import com.example.chating.global.security.UserPrincipal;
import com.example.chating.global.util.ResponseUtil;
import com.example.chating.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatRoomCreateResponse>> createChatRoom(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ChatRoomCreateRequest request
    ) {
        ChatRoomCreateResponse response = chatService.createChatRoom(principal.getUserId(), request);
        ApiResponse<ChatRoomCreateResponse> apiResponse = ResponseUtil.success("create chat room", response);

        return ResponseEntity.status(201).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ChatRoomListSearchResponses>> getMyChatRooms(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        ChatRoomListSearchResponses response = chatService.getMyChatRooms(principal.getUserId());
        ApiResponse<ChatRoomListSearchResponses> apiResponse = ResponseUtil.success("select my chat rooms", response);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{chat-room-id}/messages")
    public ResponseEntity<ApiResponse<ChatMessageHistorySearchResponses>> getChatMessages(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("chat-room-id") Long chatRoomId
    ) {
        ChatMessageHistorySearchResponses response = chatService.getChatMessages(principal.getUserId(), chatRoomId);
        ApiResponse<ChatMessageHistorySearchResponses> apiResponse = ResponseUtil.success("select chat messages", response);

        return ResponseEntity.ok(apiResponse);
    }
}
