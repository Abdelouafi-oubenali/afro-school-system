package org.example.userservice.dto;

import lombok.Data;

@Data
public class AdminResponseDTO extends UserResponseDTO {
    private String matricule;
}
