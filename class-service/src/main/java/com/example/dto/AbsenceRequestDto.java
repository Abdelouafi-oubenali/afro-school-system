package com.example.dto;

import com.example.enums.Type;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbsenceRequestDto {

    @NotNull(message = "L'élève est obligatoire")
    private UUID eleveId;

    @NotNull(message = "L'enseignant est obligatoire")
    private UUID enseignantId;

    @NotNull(message = "La séance est obligatoire")
    private UUID seanceId;

    @NotNull(message = "La classe est obligatoire")
    private UUID classeId;

    @NotNull(message = "La date est obligatoire")
    private LocalDate date;

    private LocalTime heureDebut;

    private LocalTime heureFin;

    @NotNull(message = "Le type d'absence est obligatoire")
    private Type type;

    private String motif;
}
