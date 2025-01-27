package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserInfoResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.exception.error.ErrorCode;
import org.example.expert.exception.exception.BadRequestException;
import org.example.expert.exception.exception.NotFoundException;
import org.example.expert.exception.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfoResponse getUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        return new UserInfoResponse(user.getId(), user.getNickname(), user.getEmail());
    }

    public List<UserInfoResponse> getUserByNickname(String nickname) {
        List<User> userList = userRepository.findByNickname(nickname);

        return userList.stream()
                .map(UserInfoResponse::of).toList();
    }

    @Transactional
    public void changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {
        validateNewPassword(userChangePasswordRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (passwordEncoder.matches(userChangePasswordRequest.newPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorCode.SAME_PASSWORD_EXCEPTION);
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.oldPassword(), user.getPassword())) {
            throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD_EXCEPTION);
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.newPassword()));
    }

    private void validateNewPassword(UserChangePasswordRequest userChangePasswordRequest) {
        if (userChangePasswordRequest.newPassword().length() < 8 ||
                !userChangePasswordRequest.newPassword().matches(".*\\d.*") ||
                !userChangePasswordRequest.newPassword().matches(".*[A-Z].*")) {
            throw new BadRequestException(ErrorCode.WRONG_PASSWORD_PATTERN);
        }
    }
}
