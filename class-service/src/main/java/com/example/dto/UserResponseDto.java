package com.example.dto;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;

@Data
public class UserResponseDto {
    private UUID id;
    private String nom;
    private String prenom;
    private String email;
    private String phone;
    private LocalDate dateNaissance;
    private String role;
}
