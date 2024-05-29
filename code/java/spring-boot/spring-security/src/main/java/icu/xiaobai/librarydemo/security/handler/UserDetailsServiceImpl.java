package icu.xiaobai.librarydemo.security.handler;

import icu.xiaobai.librarydemo.entity.User;
import icu.xiaobai.librarydemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 我们使用邮箱作为用户名，对用户身份进行识别
        User user = userService.getUserByEmail(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("该用户不存在");
        }

        return user;
    }
}
