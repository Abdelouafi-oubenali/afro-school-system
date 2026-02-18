package com.example.controller;

import com.example.dto.MatiereRequestDto;
import com.example.dto.MatiereResponseDto;
import com.example.service.MatiereService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/matieres")
public class MatiereController {

    private final MatiereService matiereService;

    public MatiereController(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    @PostMapping
    public ResponseEntity<MatiereResponseDto> createMatiere(@Valid @RequestBody MatiereRequestDto matiereRequestDto) {
        MatiereResponseDto matiere = matiereService.createMatiere(matiereRequestDto);
        return new ResponseEntity<>(matiere, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatiereResponseDto> getMatiereById(@PathVariable UUID id) {
        MatiereResponseDto matiere = matiereService.getMatiereById(id);
        return ResponseEntity.ok(matiere);
    }

    @GetMapping
    public ResponseEntity<List<MatiereResponseDto>> getAllMatieres() {
        List<MatiereResponseDto> matieres = matiereService.getAllMatieres();
        return ResponseEntity.ok(matieres);
    }

    @GetMapping("/actives")
    public ResponseEntity<List<MatiereResponseDto>> getAllMatieresActives() {
        List<MatiereResponseDto> matieres = matiereService.getAllMatieresActives();
        return ResponseEntity.ok(matieres);
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<MatiereResponseDto> getMatiereByNom(@PathVariable String nom) {
        MatiereResponseDto matiere = matiereService.getMatiereByNom(nom);
        return ResponseEntity.ok(matiere);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatiereResponseDto> updateMatiere(
            @PathVariable UUID id,
            @Valid @RequestBody MatiereRequestDto matiereRequestDto) {
        MatiereResponseDto matiere = matiereService.updateMatiere(id, matiereRequestDto);
        return ResponseEntity.ok(matiere);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatiere(@PathVariable UUID id) {
        matiereService.deleteMatiere(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateMatiere(@PathVariable UUID id) {
        matiereService.deactivateMatiere(id);
        return ResponseEntity.noContent().build();
    }
}
