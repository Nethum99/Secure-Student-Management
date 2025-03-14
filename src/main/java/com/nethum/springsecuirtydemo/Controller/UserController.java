package com.nethum.springsecuirtydemo.Controller;

import com.nethum.springsecuirtydemo.model.User;
import com.nethum.springsecuirtydemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public User register(@RequestBody User user){
        userService.saveUser(user);
        return user;
    }
}
