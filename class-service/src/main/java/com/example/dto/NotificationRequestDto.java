package com.example.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class NotificationRequestDto {
    private UUID userId;
    private String title;
    private String content;
    private String type;
}
