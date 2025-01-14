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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("login error: {}", exception.getMessage());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(ErrorCode.LOGIN_FAILED_EXCEPTION.getStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(
                new ErrorResponse(ErrorCode.LOGIN_FAILED_EXCEPTION.getStatus(), exception.getMessage())));
    }
}
