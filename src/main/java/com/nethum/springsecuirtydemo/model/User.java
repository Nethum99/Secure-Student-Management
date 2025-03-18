package com.nethum.springsecuirtydemo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")  // Explicitly mapping the column name
    private String userName;

    @Column(name = "password")  // Explicitly mapping the column name
    private String password;

    @Column(name = "role")  // New column to store the role of the user
    private String role;  // Example: "USER" or "ADMIN"

}
