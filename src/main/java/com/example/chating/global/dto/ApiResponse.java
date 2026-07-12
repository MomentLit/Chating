package com.example.chating.global.dto;

public record ApiResponse<T>(
        String message,
        T data
) {
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(message, null);
    }
}
