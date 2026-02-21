
package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class NoteRequestDto {
    @NotNull
    private UUID eleveId;

    private UUID matiereId;

    private UUID classeId;

    @NotNull
    private Double valeur;

    private String commentaire;

    private UUID enseignantId;

    private Integer numeroExamen;
}
