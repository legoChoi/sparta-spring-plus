package org.example.expert.domain.manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.log.service.LogService;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.exception.error.ErrorCode;
import org.example.expert.exception.exception.BadRequestException;
import org.example.expert.exception.exception.ForbiddenException;
import org.example.expert.exception.exception.NotFoundException;
import org.example.expert.security.entity.CustomUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final LogService logService;

    @Transactional
    public ManagerSaveResponse saveManager(Long userId, long todoId, ManagerSaveRequest managerSaveRequest) {
        logService.save(userId);

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TODO_NOT_FOUND));

        if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(userId, todo.getUser().getId())) {
            throw new ForbiddenException(ErrorCode.AUTHORIZATION_EXCEPTION);
        }

        User managerUser = userRepository.findById(managerSaveRequest.managerUserId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MANAGER_NOT_FOUND));

        if (ObjectUtils.nullSafeEquals(userId, managerUser.getId())) {
            throw new BadRequestException(ErrorCode.MANAGER_SELF_ASSIGNMENT_EXCEPTION);
        }

        Manager newManagerUser = new Manager(managerUser, todo);
        Manager savedManagerUser = managerRepository.save(newManagerUser);

        todo.incrementManagerCount();

        return new ManagerSaveResponse(
                savedManagerUser.getId(),
                new UserResponse(managerUser.getId(), managerUser.getNickname(), managerUser.getEmail())
        );
    }

    public List<ManagerResponse> getManagers(long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TODO_NOT_FOUND));

        List<Manager> managerList = managerRepository.findByTodoIdWithUser(todo.getId());

        List<ManagerResponse> dtoList = new ArrayList<>();
        for (Manager manager : managerList) {
            User user = manager.getUser();
            dtoList.add(new ManagerResponse(
                    manager.getId(),
                    new UserResponse(user.getId(), user.getNickname(), user.getEmail())
            ));
        }
        return dtoList;
    }

    @Transactional
    public void deleteManager(CustomUserDetails userDetails, long todoId, long managerId) {
        User user = User.fromUserDetails(userDetails);

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TODO_NOT_FOUND));

        if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
            throw new ForbiddenException(ErrorCode.AUTHORIZATION_EXCEPTION);
        }

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MANAGER_NOT_FOUND));

        if (!ObjectUtils.nullSafeEquals(todo.getId(), manager.getTodo().getId())) {
            throw new BadRequestException(ErrorCode.NOT_ASSIGNED_MANAGER_EXCEPTION);
        }

        managerRepository.delete(manager);
        todo.decrementManagerCount();
    }
}
