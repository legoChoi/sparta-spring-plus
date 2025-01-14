package org.example.expert.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.exception.error.ErrorCode;
import org.example.expert.exception.error.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("Security Authentication error: need to login");

        ErrorCode error = ErrorCode.NEED_LOGIN_EXCEPTION;

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(error.getStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(
                new ErrorResponse(error.getStatus(), error.getMessage())));
    }
}
