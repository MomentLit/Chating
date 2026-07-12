package com.example.chating.global.security;

import lombok.Getter;

@Getter
public class UserPrincipal {

    private final String userId;

    private final String role;

    public UserPrincipal(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }
}
