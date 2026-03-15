package com.example.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class AdminResponseDto {
    private UUID id;
    private String nom;
    private String prenom;
    private String email;
}
