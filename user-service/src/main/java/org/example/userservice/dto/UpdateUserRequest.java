package org.example.userservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String phone;
    private LocalDate dateNaissance;
}