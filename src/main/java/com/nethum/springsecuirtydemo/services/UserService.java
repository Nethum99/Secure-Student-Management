package com.nethum.springsecuirtydemo.services;

import com.nethum.springsecuirtydemo.model.User;
import com.nethum.springsecuirtydemo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // Update User
    public void updateUser(User user) {
        User existingUser = userRepo.findByUserName(user.getUserName());
        if (existingUser != null) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); // Encrypt password
            existingUser.setUserName(user.getUserName());
            userRepo.save(existingUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }



    public void updateUserAsAdmin(int id, User user) {
        User existingUser = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setUserName(user.getUserName());
        existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); // Admin can update all details
        userRepo.save(existingUser);
    }

    public void deleteUser(int id) {
        userRepo.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

}
