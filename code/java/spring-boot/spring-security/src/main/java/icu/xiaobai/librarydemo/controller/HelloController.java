package icu.xiaobai.librarydemo.controller;

import icu.xiaobai.librarydemo.entity.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public Response<String> hello() {
        return new Response<>(HttpStatus.OK, "HelloWorld");
    }
}
