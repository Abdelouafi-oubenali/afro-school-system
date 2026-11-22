package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeanceRequestDto {

    @NotNull(message = "La classe est obligatoire")
    private UUID classeId;

    @NotNull(message = "La matière est obligatoire")
    private UUID matiereId;

    @NotNull(message = "L'enseignant est obligatoire")
    private UUID enseignantId;

    @NotNull(message = "Le jour est obligatoire")
    private DayOfWeek jour;

    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime heureFin;
}
