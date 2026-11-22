package org.example.userservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.userservice.enums.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
@Data
public abstract class User {

    @Id
    private UUID id;
    
    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;

    private LocalDate dateNaissance;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean active = true;
}
