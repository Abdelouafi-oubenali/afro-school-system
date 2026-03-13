package com.example.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MatiereResponseDto {
    private UUID id;
    private String nom;
    private String description;
    private Double coefficient;
    private boolean estActif;
}
