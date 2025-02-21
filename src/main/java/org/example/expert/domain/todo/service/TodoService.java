package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.exception.error.ErrorCode;
import org.example.expert.exception.exception.ForbiddenException;
import org.example.expert.exception.exception.NotFoundException;
import org.example.expert.security.entity.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    @Transactional
    public TodoSaveResponse saveTodo(CustomUserDetails userDetails, TodoSaveRequest todoSaveRequest) {
        User user = User.fromUserDetails(userDetails);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.title(),
                todoSaveRequest.contents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getNickname(), user.getEmail())
        );
    }

    public Page<TodoResponse> getTodos(int page, int size, String weather, LocalDate startDate, LocalDate endDate) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "modifiedAt");

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (startDate != null && endDate != null) {
            startDateTime = startDate.atStartOfDay();
            endDateTime = endDate.atTime(LocalTime.MAX);
        }

        Page<Todo> todos = null;

        if (weather == null && (startDateTime == null || endDateTime == null)) {
            todos = todoRepository.findAll(pageable);
        }

        if (weather != null && (startDateTime != null && endDateTime != null)) {
            todos = todoRepository.findAllByWeatherAndModifiedAtBetween(weather, startDateTime, endDateTime, pageable);
        }

        if (weather != null) {
            todos = todoRepository.findAllByWeather(weather, pageable);
        }

        if (startDateTime != null && endDateTime != null) {
            todos = todoRepository.findAllByModifiedAtBetween(startDateTime, endDateTime, pageable);
        }


        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getNickname(), todo.getUser().getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }

    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TODO_NOT_FOUND));

        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getNickname(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }

    public Page<TodoSearchResponse> search(int page, int size, String title, String nickname, LocalDate startDate, LocalDate endDate) {
        Pageable pageable = PageRequest.of(page - 1, size);
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

        return todoRepository.search(title, nickname, startDateTime, endDateTime, pageable);
    }

    @Transactional
    public void deleteTodo(Long userId, long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TODO_NOT_FOUND));

        if (userId != todo.getUser().getId()) {
            throw new ForbiddenException(ErrorCode.AUTHORIZATION_EXCEPTION);
        }

        todoRepository.delete(todo);
    }
}
