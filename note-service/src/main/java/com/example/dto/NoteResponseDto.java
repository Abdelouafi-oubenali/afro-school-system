package com.example.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NoteResponseDto {
    private UUID id;
    private UUID eleveId;
    private UUID matiereId;
    private UUID classeId;
    private Double valeur;
    private String commentaire;
    private LocalDateTime createdAt;
    private UUID enseignantId;
    private Integer numeroExamen;
}
