package org.example.expert.domain.auth.dto.response;


public record AuthSignupResponse(

        String accessToken,
        String refreshToken
) {
}
