package com.example.dto;

import com.example.entity.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class NotificationRequestDto {

    @NotNull(message = "userId est obligatoire")
    private UUID userId;

    @NotBlank(message = "title est obligatoire")
    private String title;

    @NotBlank(message = "content est obligatoire")
    private String content;

    private NotificationType type;
}
