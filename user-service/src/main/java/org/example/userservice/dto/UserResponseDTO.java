package org.example.userservice.dto;

import lombok.Data;
import org.example.userservice.enums.Role;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserResponseDTO {

    private UUID id;
    private String nom;
    private String prenom;
    private String email;
    private String phone;
    private LocalDate dateNaissance;
    private Role role;
}
