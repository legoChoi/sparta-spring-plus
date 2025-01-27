package org.example.expert.domain.auth.dto.response;


public record AuthSignInResponse(

        String accessToken,
        String refreshToken
) {
}
