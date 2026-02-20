package com.example.dto;

import com.example.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbsenceResponseDto {

    private UUID id;
    private UUID eleveId;
    private UUID enseignantId;
    private UUID seanceId;
    private UUID classeId;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Type type;
    private String motif;
}
