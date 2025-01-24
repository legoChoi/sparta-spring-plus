package org.example.expert.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.entity.RedisRefreshToken;
import org.example.expert.domain.auth.repository.RedisRefreshTokenRepository;
import org.example.expert.security.entity.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final RedisRefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userCustom = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtUtil.generateAccessToken(userCustom.getId(), userCustom.getEmail(), userCustom.getUsername(), userCustom.getUserRole());
        String refreshToken = jwtUtil.generateRefreshToken(userCustom.getId());

        RedisRefreshToken redisRefreshToken = new RedisRefreshToken(userCustom.getId(), refreshToken);
        refreshTokenRepository.save(redisRefreshToken);

        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(new SigninResponse(accessToken, refreshToken)));
    }
}
