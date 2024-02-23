package com.EventCrafters.EventCrafters.service;

import com.EventCrafters.EventCrafters.model.User;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

public class TokenService {
    private String token;
    private LocalDateTime dueDate;
    private User user;

    public TokenService(User user) {
        this.user = user;
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        this.token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        dueDate = LocalDateTime.now().plusMinutes(10);
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public boolean isValid() {
        return LocalDateTime.now().isBefore(dueDate);
    }
}
