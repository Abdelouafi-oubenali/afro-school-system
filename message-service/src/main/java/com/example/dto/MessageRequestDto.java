package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class MessageRequestDto {

    @NotNull(message = "senderId est obligatoire")
    private UUID senderId;

    @NotNull(message = "receiverId est obligatoire")
    private UUID receiverId;

    @NotBlank(message = "content est obligatoire")
    private String content;
}
