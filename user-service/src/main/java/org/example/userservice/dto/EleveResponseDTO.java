package org.example.userservice.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class EleveResponseDTO extends UserResponseDTO {
    private UUID classe;
}
