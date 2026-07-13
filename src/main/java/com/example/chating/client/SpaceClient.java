package com.example.chating.client;

import com.example.chating.client.dto.SpaceDetailResponse;
import com.example.chating.global.dto.ApiResponse;
import com.example.chating.global.exception.SpaceClientException;
import com.example.chating.global.exception.SpaceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class SpaceClient {

    private final RestClient restClient;

    public SpaceClient(
            @Value("${space-service.base-url}") String baseUrl
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public SpaceDetailResponse getSpace(Long spaceId) {
        ApiResponse<SpaceDetailResponse> response = restClient.get()
                .uri("/spaces/{spaceId}", spaceId)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, res) -> {
                    throw new SpaceNotFoundException("공간을 찾을 수 없습니다.");
                })
                .body(new ParameterizedTypeReference<ApiResponse<SpaceDetailResponse>>() {
                });

        if (response == null || response.data() == null) {
            throw new SpaceClientException("공간 정보를 조회할 수 없습니다.");
        }

        return response.data();
    }
}
