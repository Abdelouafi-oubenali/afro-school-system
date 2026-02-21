package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import com.example.dto.NoteRequestDto;
import com.example.dto.NoteResponseDto;
import com.example.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public ResponseEntity<List<NoteResponseDto>> getAll() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @GetMapping("/eleve/{eleveId}")
    public ResponseEntity<List<NoteResponseDto>> getByEleve(@PathVariable("eleveId") java.util.UUID eleveId) {
        return ResponseEntity.ok(noteService.getNotesByEleve(eleveId));
    }

    @GetMapping("/classe/{classeId}")
    public ResponseEntity<List<NoteResponseDto>> getByClasse(@PathVariable("classeId") java.util.UUID classeId) {
        return ResponseEntity.ok(noteService.getNotesByClasse(classeId));
    }

    @GetMapping("/eleve/{eleveId}/matiere/{matiereId}")
    public ResponseEntity<List<NoteResponseDto>> getByEleveAndMatiere(
            @PathVariable("eleveId") java.util.UUID eleveId,
            @PathVariable("matiereId") java.util.UUID matiereId) {
        return ResponseEntity.ok(noteService.getNotesByEleveAndMatiere(eleveId, matiereId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseDto> getById(@PathVariable("id") java.util.UUID id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @PostMapping
    public ResponseEntity<NoteResponseDto> create(@Valid @RequestBody NoteRequestDto request) {
        return ResponseEntity.ok(noteService.createNote(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseDto> update(@PathVariable("id") java.util.UUID id, @Valid @RequestBody NoteRequestDto request) {
        return ResponseEntity.ok(noteService.updateNote(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") java.util.UUID id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
