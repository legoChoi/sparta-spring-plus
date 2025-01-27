package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserInfoResponse;
import org.example.expert.domain.user.service.UserService;
import org.example.expert.security.entity.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserInfoResponse> getUser(
            @PathVariable long userId
    ) {
        return ResponseEntity.ok()
                .body(userService.getUser(userId));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserInfoResponse>> getUserByNickname(
            @RequestParam String nickname
    ) {
        return ResponseEntity.ok()
                .body(userService.getUserByNickname(nickname));
    }

    @PutMapping("/users")
    public void changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserChangePasswordRequest userChangePasswordRequest
    ) {
        userService.changePassword(userDetails.getId(), userChangePasswordRequest);
    }
}
