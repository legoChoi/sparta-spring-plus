package org.example.expert.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthSignupRequest(

        @NotBlank @Email
        String email,

        @NotBlank
        String password,

        @NotBlank
        String nickname,

        @NotBlank
        String userRole
) {
}
