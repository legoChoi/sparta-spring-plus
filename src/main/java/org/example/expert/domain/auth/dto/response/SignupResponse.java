package org.example.expert.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SignupResponse {

    private final String accessToken;
    private final String refreshToken;

    public SignupResponse(String bearerToken, String refreshToken) {
        this.accessToken = bearerToken;
        this.refreshToken = refreshToken;
    }
}
