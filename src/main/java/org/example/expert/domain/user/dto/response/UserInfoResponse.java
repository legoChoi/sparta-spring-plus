package org.example.expert.domain.user.dto.response;

public record UserInfoResponse(

        Long id,
        String nickname,
        String email
) {
}
