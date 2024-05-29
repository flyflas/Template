package icu.xiaobai.librarydemo.security.handler;

import icu.xiaobai.librarydemo.entity.User;
import icu.xiaobai.librarydemo.service.redis.RedisUserService;
import icu.xiaobai.librarydemo.util.JwtUtil;
import icu.xiaobai.librarydemo.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    private final ResponseUtil responseUtil;
    private final RedisUserService redisUserService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationSuccessHandlerImpl(ResponseUtil responseUtil, RedisUserService redisUserService, JwtUtil jwtUtil) {
        this.responseUtil = responseUtil;
        this.redisUserService = redisUserService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 获取用户，生成JWT
        User user = (User) authentication.getPrincipal();
        redisUserService.saveUser(user);
        String jwt = jwtUtil.createJWT(user.getEmail());
        responseUtil.sendResponse(response, HttpStatus.OK, jwt);
    }
}
