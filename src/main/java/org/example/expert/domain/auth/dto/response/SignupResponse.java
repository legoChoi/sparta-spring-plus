package org.example.expert.domain.auth.dto.response;


public record SignupResponse(

        String accessToken,
        String refreshToken
) {
}
