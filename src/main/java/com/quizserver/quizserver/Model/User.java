package com.quizserver.quizserver.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quizserver.quizserver.emuns.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private UserRole role;

    @OneToOne(mappedBy ="user",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private UserProfile userProfile;
}
