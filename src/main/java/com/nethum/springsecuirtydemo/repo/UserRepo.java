package com.nethum.springsecuirtydemo.repo;

import com.nethum.springsecuirtydemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByUserName(String userName);
}
