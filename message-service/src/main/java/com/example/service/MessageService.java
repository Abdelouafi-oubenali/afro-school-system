package com.example.service;

import com.example.dto.MessageRequestDto;
import com.example.dto.MessageResponseDto;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageResponseDto create(MessageRequestDto request);
    MessageResponseDto getById(UUID id);
    List<MessageResponseDto> getAll();
    List<MessageResponseDto> getByReceiverId(UUID receiverId);
    List<MessageResponseDto> getBySenderId(UUID senderId);
    MessageResponseDto markAsRead(UUID id);
}
