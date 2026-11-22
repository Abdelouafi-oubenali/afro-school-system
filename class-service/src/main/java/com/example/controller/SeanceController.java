package com.example.controller;

import com.example.dto.SeanceRequestDto;
import com.example.dto.SeanceResponseDto;
import com.example.service.SeanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/seances")
public class SeanceController {

    private final SeanceService seanceService;

    public SeanceController(SeanceService seanceService) {
        this.seanceService = seanceService;
    }

    @PostMapping
    public ResponseEntity<SeanceResponseDto> createSeance(@Valid @RequestBody SeanceRequestDto seanceRequestDto) {
        SeanceResponseDto seance = seanceService.createSeance(seanceRequestDto);
        return new ResponseEntity<>(seance, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeanceResponseDto> getSeanceById(@PathVariable UUID id) {
        SeanceResponseDto seance = seanceService.getSeanceById(id);
        return ResponseEntity.ok(seance);
    }

    @GetMapping
    public ResponseEntity<List<SeanceResponseDto>> getAllSeances() {
        List<SeanceResponseDto> seances = seanceService.getAllSeances();
        return ResponseEntity.ok(seances);
    }

    @GetMapping("/actives")
    public ResponseEntity<List<SeanceResponseDto>> getSeancesActives() {
        List<SeanceResponseDto> seances = seanceService.getSeancesActives();
        return ResponseEntity.ok(seances);
    }

    @GetMapping("/classe/{classeId}")
    public ResponseEntity<List<SeanceResponseDto>> getSeancesByClasse(@PathVariable UUID classeId) {
        List<SeanceResponseDto> seances = seanceService.getSeancesByClasse(classeId);
        return ResponseEntity.ok(seances);
    }

    @GetMapping("/matiere/{matiereId}")
    public ResponseEntity<List<SeanceResponseDto>> getSeancesByMatiere(@PathVariable UUID matiereId) {
        List<SeanceResponseDto> seances = seanceService.getSeancesByMatiere(matiereId);
        return ResponseEntity.ok(seances);
    }

    @GetMapping("/enseignant/{enseignantId}")
    public ResponseEntity<List<SeanceResponseDto>> getSeancesByEnseignant(@PathVariable UUID enseignantId) {
        List<SeanceResponseDto> seances = seanceService.getSeancesByEnseignant(enseignantId);
        return ResponseEntity.ok(seances);
    }

    @GetMapping("/jour/{jour}")
    public ResponseEntity<List<SeanceResponseDto>> getSeancesByJour(@PathVariable DayOfWeek jour) {
        List<SeanceResponseDto> seances = seanceService.getSeancesByJour(jour);
        return ResponseEntity.ok(seances);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeanceResponseDto> updateSeance(
            @PathVariable UUID id,
            @Valid @RequestBody SeanceRequestDto seanceRequestDto) {
        SeanceResponseDto seance = seanceService.updateSeance(id, seanceRequestDto);
        return ResponseEntity.ok(seance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeance(@PathVariable UUID id) {
        seanceService.deleteSeance(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateSeance(@PathVariable UUID id) {
        seanceService.deactivateSeance(id);
        return ResponseEntity.noContent().build();
    }
}
