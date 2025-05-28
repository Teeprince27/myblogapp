package com.temitope.myblogapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.temitope.myblogapp.dto.response.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.info("Request URI: {}", request.getRequestURI());
        log.info("AuthenticationException: {}", authException.getMessage());

        String errorMessage = (String) request.getAttribute("error-message");
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = "Unauthorized access. Please check your credentials.";
        }

        CommonResponse errorResponse = CommonResponse.error(errorMessage);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));


        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }

}
