package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "class-service")
public interface MatiereClient {

    @GetMapping("/api/matieres/{id}")
    void getMatiereById(@PathVariable("id") UUID id);

}
