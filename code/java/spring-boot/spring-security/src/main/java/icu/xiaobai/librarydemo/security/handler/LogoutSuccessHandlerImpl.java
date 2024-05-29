package icu.xiaobai.librarydemo.security.handler;

import icu.xiaobai.librarydemo.entity.User;
import icu.xiaobai.librarydemo.service.redis.RedisUserService;
import icu.xiaobai.librarydemo.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    private final ResponseUtil responseUtil;
    private final RedisUserService redisUserService;

    public LogoutSuccessHandlerImpl(ResponseUtil responseUtil, RedisUserService redisUserService) {
        this.responseUtil = responseUtil;
        this.redisUserService = redisUserService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // TODO 清除用户信息
        User user = (User) authentication.getPrincipal();
        redisUserService.clearUserInfo(user.getUserId(), user.getEmail());
        responseUtil.sendResponse(response, HttpStatus.OK, "success");
    }
}
