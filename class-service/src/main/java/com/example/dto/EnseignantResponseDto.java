package com.example.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class EnseignantResponseDto extends UserResponseDto {
    private String specialite;
    private LocalDate dateEmbauche;
}
    