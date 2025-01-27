package org.example.expert.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.auth.dto.request.AuthSignInRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    private static final String LOGIN_HTTP_METHOD = "POST";
    private static final String LOGIN_URL = "/auth/signin";

    public CustomAuthenticationFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(LOGIN_URL, LOGIN_HTTP_METHOD)); // check login request path
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        AuthSignInRequest authSignInRequest = objectMapper.readValue(request.getReader(), AuthSignInRequest.class);
        validateAuthLoginRequest(authSignInRequest);

        String email = authSignInRequest.email();
        String password = authSignInRequest.password();

        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    private void validateAuthLoginRequest(AuthSignInRequest authLoginRequest) {
        if (!StringUtils.hasText(authLoginRequest.email()) || !StringUtils.hasText(authLoginRequest.password())) {
            throw new BadCredentialsException("이메일과 비밀번호를 입력해주세요.");
        }
    }
}
