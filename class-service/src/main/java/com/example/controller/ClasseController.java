package com.example.controller;

import com.example.dto.ClasseRequestDto;
import com.example.dto.ClasseResponseDto;
import com.example.dto.EleveResponseDto;
import com.example.dto.EnseignantResponseDto;
import com.example.service.ClasseService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classes")
public class ClasseController {

    private final ClasseService classeService;

    public ClasseController(ClasseService classeService) {
        this.classeService = classeService;
    }

    @PostMapping
    public ResponseEntity<ClasseResponseDto> create(@Valid @RequestBody ClasseRequestDto dto) {
        ClasseResponseDto created = classeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClasseResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ClasseRequestDto dto) {
        ClasseResponseDto updated = classeService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClasseResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(classeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ClasseResponseDto>> getAll() {
        return ResponseEntity.ok(classeService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        classeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{classeId}/assign-student/{studentId}")
    public ResponseEntity<Void> assignStudentToClasse(
            @PathVariable UUID classeId,
            @PathVariable UUID studentId) {
        classeService.assignStudentToClasse(classeId, studentId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{classeId}/assign-students")
    public ResponseEntity<Void> assignStudentsToClasse(
            @PathVariable UUID classeId,
            @RequestBody(required = false) List<UUID> studentIds,
            @RequestParam(value = "studentIds", required = false) List<UUID> studentIdsFromQuery) {
        List<UUID> ids = (studentIds != null && !studentIds.isEmpty()) ? studentIds : studentIdsFromQuery;
        classeService.assignStudentsToClasse(classeId, ids);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{classeId}/students")
    public ResponseEntity<List<EleveResponseDto>> getStudentsByClasseId(@PathVariable UUID classeId) {
        return ResponseEntity.ok(classeService.getStudentsByClasseId(classeId));
    }

    @PutMapping("/{classeId}/assign-enseignant/{enseignantId}")
    public ResponseEntity<Void> assignEnseignantToClasse(
            @PathVariable UUID classeId,
            @PathVariable UUID enseignantId) {
        classeService.assignEnseignantToClasse(classeId, enseignantId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{classeId}/enseignants")
    public ResponseEntity<List<EnseignantResponseDto>> getEnseignantsByClasseId(@PathVariable UUID classeId) {
        return ResponseEntity.ok(classeService.getEnseignantsByClasseId(classeId));
    }
}
