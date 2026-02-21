package com.example.service;

import com.example.dto.NoteRequestDto;
import com.example.dto.NoteResponseDto;

import java.util.List;
import java.util.UUID;

public interface NoteService {
    NoteResponseDto createNote(NoteRequestDto request);
    NoteResponseDto getNoteById(UUID id);
    List<NoteResponseDto> getNotesByEleve(UUID eleveId);
    List<NoteResponseDto> getNotesByClasse(UUID classeId);
    List<NoteResponseDto> getNotesByEleveAndMatiere(UUID eleveId, UUID matiereId);
    List<NoteResponseDto> getAllNotes();
    NoteResponseDto updateNote(UUID id, NoteRequestDto request);
    void deleteNote(UUID id);
}
