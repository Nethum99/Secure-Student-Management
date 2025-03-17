package com.nethum.springsecuirtydemo.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("hello")
    public String greet(HttpServletRequest request) {
        return "Hello world ";
    }

    @GetMapping("about")
    public String about(HttpServletRequest request){
        return "nethum "+request.getSession().getId();
    }
}
