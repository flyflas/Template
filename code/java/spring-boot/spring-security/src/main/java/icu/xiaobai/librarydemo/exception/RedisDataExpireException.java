package icu.xiaobai.librarydemo.exception;

public class RedisDataExpireException extends BaseException{
    public RedisDataExpireException(String message) {
        super(message);
    }

    public RedisDataExpireException(String message, Throwable cause) {
        super(message, cause);
    }
}
