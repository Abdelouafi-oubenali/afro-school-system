package org.example.userservice.dto;

import lombok.Data;
import org.example.userservice.enums.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class UserDTO {

    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String phone;
    private LocalDate dateNaissance;
    private Role role;

    // Admin
    private String matricule;

    // Eleve
    private Long classe;

    // Parent
    private List<UUID> childIds;

    // Enseignant
    private String specialite;
    private LocalDate dateEmbauche;
    private List<Long> classes;
}
