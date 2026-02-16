package com.example.service;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.UUID;


@FeignClient(name = "users-service")
public interface UserClient {

    @GetMapping("/api/users/students/{id}/classe")
    UUID getClasseIdByStudent(@PathVariable("id") UUID studentId);

    @PutMapping("/api/users/students/{id}/classe/{classeId}")
    void assignClasseToStudent(@PathVariable("id") UUID studentId,
                               @PathVariable("classeId") UUID classeId);
}
