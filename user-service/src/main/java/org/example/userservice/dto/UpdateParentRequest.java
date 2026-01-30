package org.example.userservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateParentRequest {

    private String nom;
    private String prenom;
    private String email;
    private String password; // optionnel
    private String phone;
    private LocalDate dateNaissance;
    private List<UUID> childIds;
}


