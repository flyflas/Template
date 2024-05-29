package icu.xiaobai.librarydemo.service.impl;

import icu.xiaobai.librarydemo.entity.User;
import icu.xiaobai.librarydemo.mapper.UserMapper;
import icu.xiaobai.librarydemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.selectByEmail(email);
    }
}
