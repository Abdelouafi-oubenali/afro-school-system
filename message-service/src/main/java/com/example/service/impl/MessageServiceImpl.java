package com.example.service.impl;

import com.example.dto.MessageRequestDto;
import com.example.dto.MessageResponseDto;
import com.example.dto.NotificationRequestDto;
import com.example.entity.Message;
import com.example.entity.MessageStatus;
import com.example.entity.NotificationType;
import com.example.repository.MessageRepository;
import com.example.service.MessageService;
import com.example.service.NotificationService;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final NotificationService notificationService;

    public MessageServiceImpl(MessageRepository messageRepository,
                              NotificationService notificationService) {
        this.messageRepository = messageRepository;
        this.notificationService = notificationService;
    }

    @Override
    public MessageResponseDto create(MessageRequestDto request) {
        Message message = new Message();
        message.setSenderId(request.getSenderId());
        message.setReceiverId(request.getReceiverId());
        message.setContent(request.getContent());

        Message saved = messageRepository.save(message);

        // Notification automatique au destinataire
        NotificationRequestDto notification = new NotificationRequestDto();
        notification.setUserId(saved.getReceiverId());
        notification.setTitle("Nouveau message");
        notification.setContent("Vous avez reçu un nouveau message : " + saved.getContent());
        notification.setType(NotificationType.MESSAGE);
        notificationService.create(notification);

        return toDto(saved);
    }

    @Override
    public MessageResponseDto getById(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message introuvable: " + id));
        return toDto(message);
    }

    @Override
    public List<MessageResponseDto> getAll() {
        return messageRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public List<MessageResponseDto> getByReceiverId(UUID receiverId) {
        return messageRepository.findAllByReceiverIdOrderByCreatedAtDesc(receiverId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<MessageResponseDto> getBySenderId(UUID senderId) {
        return messageRepository.findAllBySenderIdOrderByCreatedAtDesc(senderId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public MessageResponseDto markAsRead(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message introuvable: " + id));
        message.setStatus(MessageStatus.READ);
        return toDto(messageRepository.save(message));
    }

    private MessageResponseDto toDto(Message message) {
        MessageResponseDto dto = new MessageResponseDto();
        dto.setId(message.getId());
        dto.setSenderId(message.getSenderId());
        dto.setReceiverId(message.getReceiverId());
        dto.setContent(message.getContent());
        dto.setStatus(message.getStatus());
        dto.setCreatedAt(message.getCreatedAt());
        return dto;
    }
}
