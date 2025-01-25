package org.example.expert.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TodoSaveRequest(

        @NotBlank
        String title,

        @NotBlank
        String contents
) {
}
