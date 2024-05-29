package icu.xiaobai.librarydemo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import icu.xiaobai.librarydemo.entity.Response;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResponseUtil {
    private final ObjectMapper objectMapper;

    @Autowired
    public ResponseUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void sendResponse(HttpServletResponse response, HttpStatus httpStatus, String info) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(objectMapper.writeValueAsString(new Response<>(httpStatus, info)));
    }
}
