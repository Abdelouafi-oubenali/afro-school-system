package org.example.userservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UpdateUserRequest {
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String phone;
    private LocalDate dateNaissance;

    private String specialite;
    private LocalDate dateEmbauche;

    // Parent
    private UUID childId;

    // Admin
    private String matricule;
}