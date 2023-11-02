package com.metalancer.backend.common.config.security;

import com.metalancer.backend.common.constants.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class MemberLoginFailService implements AuthenticationFailureHandler {
    private final String DEFAULT_FAILURE_URL = "?error=true";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException, ServletException {
        ErrorCode errorMessage = null;
        if (exception instanceof AuthenticationServiceException) {
            errorMessage = ErrorCode.LOGIN_NOT_FOUND_ID_PW;
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = ErrorCode.LOGIN_NOT_FOUND_ID_PW;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format("{\"error\": \"%s\"}", errorMessage);
        response.getWriter().write(jsonResponse);
        response.sendError(ErrorCode.LOGIN_NOT_FOUND_ID_PW.getStatus().value, ErrorCode.LOGIN_NOT_FOUND_ID_PW.getMessage());

        if (errorMessage != null) {
            log.info(errorMessage.getMessage());
        }
    }

}