package com.example.controller;

import com.example.dto.SeanceResponseDto;
import com.example.service.EmploiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emploi")
public class EmploiController {

    private final EmploiService emploiService;

    public EmploiController(EmploiService emploiService) {
        this.emploiService = emploiService;
    }

    @GetMapping("/classe/{classeId}")
    public ResponseEntity<List<SeanceResponseDto>> getEmploiByClasseId(@PathVariable String classeId) {
        List<SeanceResponseDto> seances = emploiService.getEmploiByClasseId(classeId);
        return ResponseEntity.ok(seances);
    }

    @GetMapping("/enseignant/{enseignantId}")
    public ResponseEntity<List<SeanceResponseDto>> getEmploiByEnseignantId(@PathVariable String enseignantId) {
        List<SeanceResponseDto> seances = emploiService.getEmploiByEnseignantId(enseignantId);
        return ResponseEntity.ok(seances);
    }

    @GetMapping("/enseignants")
    public ResponseEntity<List<SeanceResponseDto>> getAllEmploiEnseignants() {
        List<SeanceResponseDto> seances = emploiService.getAllEmploiEnseignants();
        return ResponseEntity.ok(seances);
    }

    @GetMapping("/classes")
    public ResponseEntity<List<SeanceResponseDto>> getAllEmploiClasses() {
        List<SeanceResponseDto> seances = emploiService.getAllEmploiClasses();
        return ResponseEntity.ok(seances);
    }
}
