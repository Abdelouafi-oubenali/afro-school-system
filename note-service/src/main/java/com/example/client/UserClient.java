package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "users-service")
public interface UserClient {

    @GetMapping("/api/users/eleve/{id}")
    void getEleveById(@PathVariable("id") UUID id);

    @GetMapping("/api/users/enseignent/{id}")
    void getEnseignantById(@PathVariable("id") UUID id);

    @GetMapping("/api/users/class/{classId}")
    void getStudentsByClasseId(@PathVariable("classId") UUID classId);
}
