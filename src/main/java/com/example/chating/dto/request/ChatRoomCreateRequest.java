package com.example.chating.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record ChatRoomCreateRequest(
        @NotNull
        @JsonProperty("space_id")
        Long spaceId
) {
}
