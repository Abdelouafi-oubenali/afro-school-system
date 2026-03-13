package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoyenneMatiereDto {
    private UUID matiereId;
    private String nomMatiere;
    private Double coefficient;
    private Double moyenneDevoir;
    private Double moyenneExamen;
    private Double moyenneFinaleMatiere;
}
