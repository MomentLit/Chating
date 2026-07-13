package com.example.chating.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ChatMessageHistorySearchResponses(
        @JsonProperty("chat_room_id")
        Long chatRoomId,

        List<ChatMessageHistorySearchResponse> messages
) {
}
