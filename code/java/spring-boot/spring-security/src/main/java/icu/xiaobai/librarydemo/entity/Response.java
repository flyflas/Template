package icu.xiaobai.librarydemo.entity;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response<T> {
    private Integer code;
    private String message;
    private T data;

    public Response(HttpStatus httpStatus, T data ) {
        this.data = data;
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
    }

    public Response(HttpStatus httpStatus) {
        this(httpStatus, null);
    }


}
