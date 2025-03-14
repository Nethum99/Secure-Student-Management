package com.nethum.springsecuirtydemo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    private int id;

    @Column(name = "username")  // Explicitly mapping the column name
    private String userName;

    @Column(name = "password")  // Explicitly mapping the column name
    private String password;
}
