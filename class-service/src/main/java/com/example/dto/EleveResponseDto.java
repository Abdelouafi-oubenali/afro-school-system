package com.example.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class EleveResponseDto extends UserResponseDto {
    private UUID classe;
}
