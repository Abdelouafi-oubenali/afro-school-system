package org.example.userservice.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private long expiresIn;
    private UUID userId;

    public TokenResponse() {}

    public TokenResponse(String accessToken, String refreshToken, long expiresIn, UUID userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.userId = userId;
    }

    public TokenResponse(String accessToken, UUID userId) {
        this.accessToken = accessToken;
        this.userId = userId;
    }

}