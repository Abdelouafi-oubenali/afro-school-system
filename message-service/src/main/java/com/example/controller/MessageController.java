package com.example.controller;

import com.example.dto.MessageRequestDto;
import com.example.dto.MessageResponseDto;
import com.example.service.MessageService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<MessageResponseDto> create(@Valid @RequestBody MessageRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<MessageResponseDto>> getAll() {
        return ResponseEntity.ok(messageService.getAll());
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<MessageResponseDto>> getByReceiverId(@PathVariable UUID receiverId) {
        return ResponseEntity.ok(messageService.getByReceiverId(receiverId));
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<MessageResponseDto>> getBySenderId(@PathVariable UUID senderId) {
        return ResponseEntity.ok(messageService.getBySenderId(senderId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<MessageResponseDto> markAsRead(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.markAsRead(id));
    }
}
