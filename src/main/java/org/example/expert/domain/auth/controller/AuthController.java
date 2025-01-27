package org.example.expert.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.auth.dto.request.AuthReissueRequest;
import org.example.expert.domain.auth.dto.request.AuthSignupRequest;
import org.example.expert.domain.auth.dto.response.AuthReissueResponse;
import org.example.expert.domain.auth.dto.response.AuthSignupResponse;
import org.example.expert.domain.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<AuthSignupResponse> signup(
            @Valid @RequestBody AuthSignupRequest signupRequest
    ) {
        return ResponseEntity.ok()
                .body(authService.signup(signupRequest));
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<AuthReissueResponse> reissue(
            @RequestBody AuthReissueRequest authReissueRequest
    ) {
        return ResponseEntity.ok()
                .body(authService.reissueToken(authReissueRequest));
    }
}
