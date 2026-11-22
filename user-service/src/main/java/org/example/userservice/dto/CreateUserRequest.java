package org.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Nom est obligatoire")
    private String nom;

    @NotBlank(message = "Prenom est obligatoire")
    private String prenom;

    @Email(message = "Email doit être valide")
    @NotBlank(message = "Email est obligatoire")
    private String email;

    @NotBlank(message = "Password est obligatoire")
    @Size(min = 6, message = "Password doit contenir au moins 6 caractères")
    private String password;

    @NotBlank(message = "Phone est obligatoire")
    private String phone;

    @NotNull(message = "Date de naissance est obligatoire")
    private LocalDate dateNaissance;

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
