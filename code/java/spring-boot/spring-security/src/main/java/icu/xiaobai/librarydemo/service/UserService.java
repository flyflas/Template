package icu.xiaobai.librarydemo.service;

import icu.xiaobai.librarydemo.entity.User;

public interface UserService {
    User getUserByEmail(String email);
}
