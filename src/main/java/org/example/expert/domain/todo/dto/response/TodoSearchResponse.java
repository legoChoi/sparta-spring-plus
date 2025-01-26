package org.example.expert.domain.todo.dto.response;

public record TodoSearchResponse(

        Long todoId,
        String title,
        Integer managerCount,
        Integer commentCount
) {
}
