package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatiereResponseDto {

    private UUID id;
    private String nom;
    private String description;
    private Double coefficient;
    private boolean estActif;
}
