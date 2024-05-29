package icu.xiaobai.librarydemo.security.handler;

import icu.xiaobai.librarydemo.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    private final ResponseUtil responseUtil;

    public AuthenticationFailureHandlerImpl(ResponseUtil responseUtil) {
        this.responseUtil = responseUtil;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        responseUtil.sendResponse(response, HttpStatus.UNAUTHORIZED, exception.getLocalizedMessage());
    }
}
