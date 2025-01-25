package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserChangePasswordRequest(

        @NotBlank
        String oldPassword,

        @NotBlank
        String newPassword
) {
}
