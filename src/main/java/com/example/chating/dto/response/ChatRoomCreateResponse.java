package com.example.chating.dto.response;

import com.example.chating.entity.ChatRoom;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatRoomCreateResponse(
        @JsonProperty("chat_room_id")
        Long chatRoomId
) {

    public static ChatRoomCreateResponse from(ChatRoom chatRoom) {
        return new ChatRoomCreateResponse(chatRoom.getId());
    }
}
