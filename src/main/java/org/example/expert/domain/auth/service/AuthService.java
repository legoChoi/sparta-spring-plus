package org.example.expert.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.AuthReissueRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new InvalidRequestException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        UserRole userRole = UserRole.of(signupRequest.getUserRole());

        User newUser = new User(
                signupRequest.getEmail(),
                encodedPassword,
                signupRequest.getNickname(),
                userRole
        );
        User savedUser = userRepository.save(newUser);

        return new SignupResponse(
                jwtUtil.generateAccessToken(savedUser.getId(), savedUser.getEmail(), savedUser.getNickname(), savedUser.getUserRole()),
                jwtUtil.generateRefreshToken(savedUser.getId())
        );
    }

    public void reissueToken(AuthReissueRequest authReissueRequest) {
        // 요청 refresh token 검증
        jwtUtil.isValid(authReissueRequest.refreshToken());

        // 요청 refresh token에서 id 추출
        Long userId = jwtUtil.getUserIdFromToken(authReissueRequest.refreshToken());

        // redis에서 해당 id에 저장된 refresh token 조회

        // refresh token 검증

        // access token, refresh token 생성

        // refresh token redis에 저장 (교체)

        // access token, refresh token 반환
    }
}
