package icu.xiaobai.librarydemo.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import icu.xiaobai.librarydemo.entity.User;
import icu.xiaobai.librarydemo.service.redis.RedisUserService;
import icu.xiaobai.librarydemo.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RedisUserService redisUserService;

    public JwtFilter(JwtUtil jwtUtil, RedisUserService redisUserService) {
        this.jwtUtil = jwtUtil;
        this.redisUserService = redisUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Token");

        // 请求中没有Token
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        User user;
        DecodedJWT decodedJWT;
        try {
            decodedJWT = jwtUtil.jwtVerify(token);
            String email = decodedJWT.getClaim("email").asString();
            user = redisUserService.getUserByEmail(email);
        } catch (Exception e) {
            // jwt 时间过期，或者Redis中的数据过期
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
