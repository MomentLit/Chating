package com.example.chating.dto.response;

import com.example.chating.entity.ChatMessage;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ChatMessageHistorySearchResponse(
        @JsonProperty("message_id")
        Long messageId,

        @JsonProperty("sender_name")
        String senderName,

        @JsonProperty("sender_id")
        String senderId,

        String content,

        @JsonProperty("is_read")
        Boolean isRead,

        @JsonProperty("created_at")
        LocalDateTime createdAt
) {

    public static ChatMessageHistorySearchResponse from(
            ChatMessage message,
            String senderName
    ) {
        return new ChatMessageHistorySearchResponse(
                message.getId(),
                senderName,
                message.getSenderId(),
                message.getContent(),
                message.getIsRead(),
                message.getCreatedAt()
        );
    }
}
