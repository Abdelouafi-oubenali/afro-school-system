package com.example.repository;

import com.example.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findAllByReceiverIdOrderByCreatedAtDesc(UUID receiverId);
    List<Message> findAllBySenderIdOrderByCreatedAtDesc(UUID senderId);
}
