package com.example.chating.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ChatRoomListSearchResponses(
        @JsonProperty("chat_rooms")
        List<ChatRoomListSearchResponse> chatRooms
) {
}
