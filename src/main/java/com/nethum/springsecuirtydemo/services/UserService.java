package com.nethum.springsecuirtydemo.services;

import com.nethum.springsecuirtydemo.model.User;
import com.nethum.springsecuirtydemo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public User saveUser(User user){

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        return userRepo.save(user);

    }
}
