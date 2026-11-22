package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeanceResponseDto {

    private UUID id;
    private UUID classeId;
    private String classeNom;
    private UUID matiereId;
    private String matiereNom;
    private UUID enseignantId;
    private DayOfWeek jour;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private boolean estActif;
}
