package org.example.userservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EnseignantResponseDTO extends UserResponseDTO {

    private String specialite;
    private LocalDate dateEmbauche;
    private List<Long> classes;
}

