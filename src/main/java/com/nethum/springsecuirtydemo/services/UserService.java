package com.nethum.springsecuirtydemo.services;

import com.nethum.springsecuirtydemo.model.User;
import com.nethum.springsecuirtydemo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User saveUser(User user){
        return userRepo.save(user);
    }
}
