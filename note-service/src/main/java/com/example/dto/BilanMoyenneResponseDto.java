package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BilanMoyenneResponseDto {
    private UUID eleveId;
    private Double moyenneGenerale;
    private Double totalCoefficients;
    private Integer nombreMatieres;
    private List<MoyenneMatiereDto> detailsParMatiere;
}
