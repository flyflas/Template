package icu.xiaobai.librarydemo.service.impl.redis;

import icu.xiaobai.librarydemo.entity.User;
import icu.xiaobai.librarydemo.exception.RedisDataExpireException;
import icu.xiaobai.librarydemo.service.redis.RedisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
public class RedisUserServiceImpl implements RedisUserService {
    private final HashMapper<Object, String, Object> hashMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String USER_KEY = "user:";
    private static final String EMAIL_ID_KEY="emailId:";
    @Value("${jwt.expire-time}")
    private Integer expireTime;

    @Autowired
    public RedisUserServiceImpl(HashMapper<Object, String, Object> hashMapper, RedisTemplate<String, Object> redisTemplate) {
        this.hashMapper = hashMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveUser(User user) {
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("非法参数!!!");
        }
        redisTemplate.opsForHash().putAll(USER_KEY + user.getUserId(), hashMapper.toHash(user));
        redisTemplate.expire(USER_KEY + user.getUserId(), Duration.ofHours(expireTime));
        this.setEmailUserId(user.getEmail(), user.getUserId());
    }

    @Override
    public User getUserById(Long userId) {
        return (User) hashMapper.fromHash(redisTemplate.<String, Object>opsForHash().entries(USER_KEY + userId));
    }

    @Override
    public User getUserByEmail(String email) {
        Long userId = getUserIdByEmail(email);
        return getUserById(userId);
    }

    @Override
    public void setEmailUserId(String email, Long id) {
        redisTemplate.opsForValue().set(EMAIL_ID_KEY + email, id);
        redisTemplate.expire(EMAIL_ID_KEY + email,  Duration.ofHours(expireTime));
    }

    @Override
    public Long getUserIdByEmail(String email) {
        Integer userId = (Integer)redisTemplate.opsForValue().get(EMAIL_ID_KEY + email);
        if (Objects.isNull(userId)) {
            throw new RedisDataExpireException("请求的数据已经过期");
        }
        return userId.longValue();
    }

    @Override
    public void clearUserInfo(Long id, String email) {
        redisTemplate.delete(USER_KEY + id);
        redisTemplate.delete(EMAIL_ID_KEY + email);
    }
}
