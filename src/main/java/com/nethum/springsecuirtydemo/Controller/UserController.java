package com.nethum.springsecuirtydemo.Controller;

import com.nethum.springsecuirtydemo.model.User;
import com.nethum.springsecuirtydemo.services.JwtService;
import com.nethum.springsecuirtydemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("register")
    public User register(@RequestBody User user){
        return userService.saveUser(user);

    }

    @PostMapping("login")   //when user login system have to generate a token
    public String login(@RequestBody User user){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));     //Before generate token system have to check if username and password correct

        if(authentication.isAuthenticated()){   //if username and password correct then create a token
            return jwtService.generateToken(user.getUserName());       //in jwtservice that method doing it and after generating token user receive it as String
        }
        else {
            return "Login failed";
        }
    }
}
