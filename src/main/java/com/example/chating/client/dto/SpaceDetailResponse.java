package com.example.chating.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpaceDetailResponse(
        @JsonProperty("space_id")
        Long spaceId,

        @JsonProperty("host_id")
        String hostId,

        String name
) {
}
