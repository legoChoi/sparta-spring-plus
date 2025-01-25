package org.example.expert.domain.todo.controller;

import org.example.expert.config.GlobalExceptionHandler;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.exception.error.ErrorCode;
import org.example.expert.exception.exception.NotFoundException;
import org.example.expert.security.entity.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @BeforeEach
    void setUp() { // 시큐리티 무시
        mockMvc = MockMvcBuilders.standaloneSetup(new TodoController(todoService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void todo_단건_조회에_성공한다() throws Exception {
        // given
        long todoId = 1L;
        String title = "title";
        CustomUserDetails userDetails = new CustomUserDetails(new User(1L, "email", UserRole.ROLE_USER));
        User user = User.fromUserDetails(userDetails);
        UserResponse userResponse = new UserResponse(user.getId(), user.getNickname(), user.getEmail());
        TodoResponse response = new TodoResponse(
                todoId,
                title,
                "contents",
                "Sunny",
                userResponse,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when
        when(todoService.getTodo(todoId)).thenReturn(response);

        // then
        mockMvc.perform(get("/todos/{todoId}", todoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId))
                .andExpect(jsonPath("$.title").value(title));
    }

    @Test
    void todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다() throws Exception {
        // given
        long todoId = 1L;

        // when
        when(todoService.getTodo(todoId))
                .thenThrow(new NotFoundException(ErrorCode.TODO_NOT_FOUND));

        // then
        mockMvc.perform(get("/todos/{todoId}", todoId)
                    .header("Authorization", "Bearer access_token")) // 헤더 추가
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(ErrorCode.TODO_NOT_FOUND.getMessage()));
    }
}
