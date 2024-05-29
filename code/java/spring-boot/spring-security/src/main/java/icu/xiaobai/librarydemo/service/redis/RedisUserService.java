package icu.xiaobai.librarydemo.service.redis;

import icu.xiaobai.librarydemo.entity.User;

public interface RedisUserService {
    void saveUser(User user);
    User getUserById(Long userId);
    User getUserByEmail(String email);

    void setEmailUserId(String email, Long id);

    Long getUserIdByEmail(String email);
    void clearUserInfo(Long id, String email);
}
