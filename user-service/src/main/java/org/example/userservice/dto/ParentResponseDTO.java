package org.example.userservice.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ParentResponseDTO extends UserResponseDTO {
    private List<UUID> childIds;
    private List<EleveResponseDTO> children;
}