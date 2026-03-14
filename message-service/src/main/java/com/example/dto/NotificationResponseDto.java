package com.example.dto;

import com.example.entity.NotificationType;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class NotificationResponseDto {
    private UUID id;
    private UUID userId;
    private String title;
    private String content;
    private NotificationType type;
    private boolean isRead;
    private Instant createdAt;
}
