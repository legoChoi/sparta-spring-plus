package org.example.expert.domain.user.dto.response;

import org.example.expert.domain.user.entity.User;

public record UserInfoResponse(

        Long id,
        String nickname,
        String email
) {

    public static UserInfoResponse of(User user) {
        return new UserInfoResponse(user.getId(), user.getNickname(), user.getEmail());
    }
}
