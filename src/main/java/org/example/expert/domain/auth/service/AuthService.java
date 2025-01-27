package org.example.expert.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.AuthReissueRequest;
import org.example.expert.domain.auth.dto.request.AuthSignupRequest;
import org.example.expert.domain.auth.dto.response.AuthReissueResponse;
import org.example.expert.domain.auth.dto.response.AuthSignupResponse;
import org.example.expert.domain.auth.entity.RedisRefreshToken;
import org.example.expert.domain.auth.repository.RedisRefreshTokenRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.exception.error.ErrorCode;
import org.example.expert.exception.exception.BadRequestException;
import org.example.expert.exception.exception.NotFoundException;
import org.example.expert.exception.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final RedisRefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthSignupResponse signup(AuthSignupRequest signupRequest) {

        if (userRepository.existsByEmail(signupRequest.email())) {
            throw new BadRequestException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.password());

        UserRole userRole = UserRole.of(signupRequest.userRole());

        User newUser = new User(
                signupRequest.email(),
                encodedPassword,
                signupRequest.nickname(),
                userRole
        );
        User savedUser = userRepository.save(newUser);

        String accessToken = jwtUtil.generateAccessToken(savedUser.getId(), savedUser.getEmail(), savedUser.getNickname(), savedUser.getUserRole());
        String refreshToken = jwtUtil.generateRefreshToken(savedUser.getId());

        RedisRefreshToken redisRefreshToken = new RedisRefreshToken(savedUser.getId(), refreshToken);
        refreshTokenRepository.save(redisRefreshToken);

        return new AuthSignupResponse(
                accessToken,
                refreshToken
        );
    }

    public AuthReissueResponse reissueToken(AuthReissueRequest authReissueRequest) {
        if (!jwtUtil.isValid(authReissueRequest.refreshToken())) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN_ERROR);
        }

        Long userId = jwtUtil.getUserIdFromToken(authReissueRequest.refreshToken());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        RedisRefreshToken redisRefreshToken =
                refreshTokenRepository.findById(user.getId())
                        .orElseThrow(() -> new NotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND_EXCEPTION));

        if (!authReissueRequest.refreshToken().equals(redisRefreshToken.getRefreshToken())) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN_ERROR);
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getNickname(), user.getUserRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        refreshTokenRepository.save(new RedisRefreshToken(user.getId(), refreshToken));

        return new AuthReissueResponse(accessToken, refreshToken);
    }
}
