package com.example.dto;

import com.example.enums.LevelClasse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClasseResponseDto {

    private UUID id;
    private String name;
    private LevelClasse levelClasse;
    private UUID enseignantPrincipal;
    private String anneeScolaire;
    private Integer capaciteMax;
    private String salleAttribuee;
}
