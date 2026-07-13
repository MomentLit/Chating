package com.example.chating.client;

import com.example.chating.client.dto.UserSearchResponse;
import com.example.chating.global.exception.UserClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(
            @Value("${user-service.base-url}") String baseUrl
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public UserSearchResponse getUser(String userId) {
        UserSearchResponse response = restClient.get()
                .uri("/internal/users/{userId}", userId)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, res) -> {
                    throw new UserClientException("유저를 찾을 수 없습니다.");
                })
                .body(UserSearchResponse.class);

        if (response == null) {
            throw new UserClientException("유저 정보를 조회할 수 없습니다.");
        }

        return response;
    }
}
