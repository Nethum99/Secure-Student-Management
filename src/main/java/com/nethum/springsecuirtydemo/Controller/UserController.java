package com.nethum.springsecuirtydemo.Controller;

import com.nethum.springsecuirtydemo.model.User;
import com.nethum.springsecuirtydemo.services.JwtService;
import com.nethum.springsecuirtydemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/user/register")
    public User register(@RequestBody User user){
        System.out.println(user);
        return userService.saveUser(user);

    }

    @PostMapping("/user/login")   //when user login system have to generate a token
    public Map<String, String> login(@RequestBody User user){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));     //Before generate token system have to check if username and password correct

        if(authentication.isAuthenticated()){   //if username and password correct then create a token
           // return jwtService.generateAccessToken(user.getUserName());       //in jwtservice that method doing it and after generating token user receive it as String

            String accessToken = jwtService.generateAccessToken(user.getUserName());
            String refreshToken = jwtService.generateRefreshToken(user.getUserName());

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            return tokens;

        }
        else {
            throw new RuntimeException("Login Failed");
        }
    }

    @PostMapping("/user/refresh-token")   // Refresh the access token
    public String refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        System.out.println(refreshToken);

        if (refreshToken != null && jwtService.validateToken(refreshToken)) {
            String userName = jwtService.extractUserName(refreshToken);
            return jwtService.generateAccessToken(userName);  // Return a new access token
        }

        throw new RuntimeException("Invalid refresh token");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<String> updateUserAsAdmin(@PathVariable int id, @RequestBody User user) {
        userService.updateUserAsAdmin(id, user);
        return ResponseEntity.ok("User updated by admin successfully");
    }



    @PutMapping("/user/update")
    public ResponseEntity<String> updateUser(@RequestBody User user, Authentication authentication) {
        String currentUser = authentication.getName(); // Get the current logged-in user
        if (!user.getUserName().equals(currentUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You cannot update another user's data.");
        }
        userService.updateUser(user);
        return ResponseEntity.ok("User updated successfully");
    }
}
