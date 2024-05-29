package icu.xiaobai.librarydemo.security.handler;

import icu.xiaobai.librarydemo.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private final ResponseUtil responseUtil;

    public AuthenticationEntryPointImpl(ResponseUtil responseUtil) {
        this.responseUtil = responseUtil;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        responseUtil.sendResponse(response, HttpStatus.FORBIDDEN, authException.getLocalizedMessage());
    }
}
