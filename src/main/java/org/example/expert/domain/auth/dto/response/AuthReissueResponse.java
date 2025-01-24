package org.example.expert.domain.auth.dto.response;

public record AuthReissueResponse(
        String accessToken,
        String refreshToken
) {
}
