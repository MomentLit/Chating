package com.example.chating.dto.response;

import com.example.chating.entity.ChatRoom;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ChatRoomListSearchResponse(
        @JsonProperty("chat_room_id")
        Long chatRoomId,

        ChatRoomSpaceResponse space,

        ChatRoomUserResponse host,

        ChatRoomUserResponse seller,

        @JsonProperty("created_at")
        LocalDateTime createdAt
) {

    public static ChatRoomListSearchResponse from(ChatRoom chatRoom) {
        return new ChatRoomListSearchResponse(
                chatRoom.getId(),
                new ChatRoomSpaceResponse(chatRoom.getSpaceId(), chatRoom.getSpaceName()),
                new ChatRoomUserResponse(chatRoom.getHostId(), chatRoom.getHostName()),
                new ChatRoomUserResponse(chatRoom.getSellerId(), chatRoom.getSellerName()),
                chatRoom.getCreatedAt()
        );
    }
}
