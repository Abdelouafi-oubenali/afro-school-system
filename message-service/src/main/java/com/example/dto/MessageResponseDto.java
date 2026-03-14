package com.example.dto;

import com.example.entity.MessageStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class MessageResponseDto {
    private UUID id;
    private UUID senderId;
    private UUID receiverId;
    private String content;
    private MessageStatus status;
    private Instant createdAt;
}
