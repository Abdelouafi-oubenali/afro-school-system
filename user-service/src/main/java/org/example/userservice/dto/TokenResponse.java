package org.example.userservice.dto;

import lombok.Data;

@Data
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private long expiresIn;

    public TokenResponse() {}

    public TokenResponse(String accessToken, String refreshToken, long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }


}