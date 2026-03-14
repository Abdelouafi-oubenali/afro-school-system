package com.example.service.impl;

import com.example.dto.NotificationRequestDto;
import com.example.dto.NotificationResponseDto;
import com.example.entity.Notification;
import com.example.repository.NotificationRepository;
import com.example.service.NotificationService;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationResponseDto create(NotificationRequestDto request) {
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setTitle(request.getTitle());
        notification.setContent(request.getContent());
        notification.setType(request.getType());

        Notification saved = notificationRepository.save(notification);
        return toDto(saved);
    }

    @Override
    public NotificationResponseDto getById(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification introuvable: " + id));
        return toDto(notification);
    }

    @Override
    public List<NotificationResponseDto> getAll() {
        return notificationRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public List<NotificationResponseDto> getByUserId(UUID userId) {
        return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public NotificationResponseDto markAsRead(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification introuvable: " + id));
        notification.setRead(true);
        return toDto(notificationRepository.save(notification));
    }

    private NotificationResponseDto toDto(Notification notification) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setTitle(notification.getTitle());
        dto.setContent(notification.getContent());
        dto.setType(notification.getType());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
