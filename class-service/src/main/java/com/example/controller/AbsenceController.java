package com.example.controller;

import com.example.dto.AbsenceRequestDto;
import com.example.dto.AbsenceBulkRequestDto;
import com.example.dto.AbsenceResponseDto;
import com.example.service.AbsenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/absences")
public class AbsenceController {

    private final AbsenceService absenceService;

    public AbsenceController(AbsenceService absenceService) {
        this.absenceService = absenceService;
    }

    @PostMapping
    public ResponseEntity<AbsenceResponseDto> createAbsence(@Valid @RequestBody AbsenceRequestDto absenceRequestDto) {
        AbsenceResponseDto absence = absenceService.createAbsence(absenceRequestDto);
        return new ResponseEntity<>(absence, HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<AbsenceResponseDto>> createAbsencesBulk(@Valid @RequestBody AbsenceBulkRequestDto absenceBulkRequestDto) {
        List<AbsenceResponseDto> absences = absenceService.createAbsencesBulk(absenceBulkRequestDto);
        return new ResponseEntity<>(absences, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbsenceResponseDto> getAbsenceById(@PathVariable UUID id) {
        AbsenceResponseDto absence = absenceService.getAbsenceById(id);
        return ResponseEntity.ok(absence);
    }

    @GetMapping
    public ResponseEntity<List<AbsenceResponseDto>> getAllAbsences() {
        List<AbsenceResponseDto> absences = absenceService.getAllAbsences();
        return ResponseEntity.ok(absences);
    }

    @GetMapping("/eleve/{eleveId}")
    public ResponseEntity<List<AbsenceResponseDto>> getAbsencesByEleve(@PathVariable UUID eleveId) {
        List<AbsenceResponseDto> absences = absenceService.getAbsencesByEleve(eleveId);
        return ResponseEntity.ok(absences);
    }

    @GetMapping("/enseignant/{enseignantId}")
    public ResponseEntity<List<AbsenceResponseDto>> getAbsencesByEnseignant(@PathVariable UUID enseignantId) {
        List<AbsenceResponseDto> absences = absenceService.getAbsencesByEnseignant(enseignantId);
        return ResponseEntity.ok(absences);
    }

    @GetMapping("/classe/{classeId}")
    public ResponseEntity<List<AbsenceResponseDto>> getAbsencesByClasse(@PathVariable UUID classeId) {
        List<AbsenceResponseDto> absences = absenceService.getAbsencesByClasse(classeId);
        return ResponseEntity.ok(absences);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<AbsenceResponseDto>> getAbsencesByDate(@PathVariable LocalDate date) {
        List<AbsenceResponseDto> absences = absenceService.getAbsencesByDate(date);
        return ResponseEntity.ok(absences);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<AbsenceResponseDto>> getAbsencesByFilters(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) UUID classeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureFin) {
        List<AbsenceResponseDto> absences = absenceService.getAbsencesByFilters(date, classeId, heureDebut, heureFin);
        return ResponseEntity.ok(absences);
    }

    @GetMapping("/eleve/{eleveId}/date/{date}")
    public ResponseEntity<List<AbsenceResponseDto>> getAbsencesByEleveAndDate(
            @PathVariable UUID eleveId,
            @PathVariable LocalDate date) {
        List<AbsenceResponseDto> absences = absenceService.getAbsencesByEleveAndDate(eleveId, date);
        return ResponseEntity.ok(absences);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AbsenceResponseDto> updateAbsence(
            @PathVariable UUID id,
            @Valid @RequestBody AbsenceRequestDto absenceRequestDto) {
        AbsenceResponseDto absence = absenceService.updateAbsence(id, absenceRequestDto);
        return ResponseEntity.ok(absence);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAbsence(@PathVariable UUID id) {
        absenceService.deleteAbsence(id);
        return ResponseEntity.noContent().build();
    }
}
