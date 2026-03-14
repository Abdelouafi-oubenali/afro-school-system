package com.example.service;

import com.example.dto.NotificationRequestDto;
import com.example.dto.NotificationResponseDto;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationResponseDto create(NotificationRequestDto request);
    NotificationResponseDto getById(UUID id);
    List<NotificationResponseDto> getAll();
    List<NotificationResponseDto> getByUserId(UUID userId);
    NotificationResponseDto markAsRead(UUID id);
}
