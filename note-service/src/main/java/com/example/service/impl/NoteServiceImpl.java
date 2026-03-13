package com.example.service.impl;

import com.example.client.MatiereClient;
import com.example.client.UserClient;
import com.example.dto.NoteRequestDto;
import com.example.dto.NoteResponseDto;
import com.example.entity.Note;
import com.example.repository.NoteRepository;
import com.example.service.NoteService;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserClient userClient;
    private final MatiereClient matiereClient;

    public NoteServiceImpl(NoteRepository noteRepository, UserClient userClient, MatiereClient matiereClient) {
        this.noteRepository = noteRepository;
        this.userClient = userClient;
        this.matiereClient = matiereClient;
    }

    @Override
    public NoteResponseDto createNote(NoteRequestDto request) {
        // validate referenced ids
        if (request.getEleveId() != null) validateEleveExists(request.getEleveId());
        if (request.getEnseignantId() != null) validateEnseignantExists(request.getEnseignantId());
        if (request.getMatiereId() != null) validateMatiereExists(request.getMatiereId());

        // validate classeId if present
        if (request.getClasseId() != null) validateClasseExists(request.getClasseId());

        Note n = new Note();
        n.setEleveId(request.getEleveId());
        n.setMatiereId(request.getMatiereId());
        n.setEnseignantId(request.getEnseignantId());
        n.setNumeroExamen(request.getNumeroExamen());
        n.setTypeNote(request.getTypeNote());
        n.setClasseId(request.getClasseId());
        n.setValeur(request.getValeur());
        n.setCommentaire(request.getCommentaire());
        Note saved = noteRepository.save(n);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public NoteResponseDto getNoteById(UUID id) {
        return noteRepository.findById(id).map(this::toDto)
            .orElseThrow(() -> new com.example.exception.ResourceNotFoundException("Note non trouvée: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponseDto> getNotesByEleve(UUID eleveId) {
        return noteRepository.findByEleveId(eleveId).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponseDto> getNotesByEleveAndMatiere(UUID eleveId, UUID matiereId) {
        return noteRepository.findByEleveIdAndMatiereId(eleveId, matiereId).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponseDto> getAllNotes() {
        return noteRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public NoteResponseDto updateNote(UUID id, NoteRequestDto request) {
        Note n = noteRepository.findById(id).orElseThrow(() -> new com.example.exception.ResourceNotFoundException("Note non trouvée: " + id));
        if (request.getEleveId() != null) {
            validateEleveExists(request.getEleveId());
            n.setEleveId(request.getEleveId());
        }
        if (request.getMatiereId() != null) {
            validateMatiereExists(request.getMatiereId());
            n.setMatiereId(request.getMatiereId());
        }
        if (request.getEnseignantId() != null) {
            validateEnseignantExists(request.getEnseignantId());
            n.setEnseignantId(request.getEnseignantId());
        }
        if (request.getClasseId() != null) {
            validateClasseExists(request.getClasseId());
            n.setClasseId(request.getClasseId());
        }
        if (request.getNumeroExamen() != null) n.setNumeroExamen(request.getNumeroExamen());
        if (request.getTypeNote() != null) n.setTypeNote(request.getTypeNote());
        if (request.getValeur() != null) n.setValeur(request.getValeur());
        if (request.getCommentaire() != null) n.setCommentaire(request.getCommentaire());
        Note saved = noteRepository.save(n);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponseDto> getNotesByClasse(UUID classeId) {
        return noteRepository.findByClasseId(classeId).stream().map(this::toDto).toList();
    }

    private void validateEleveExists(UUID eleveId) {
        try {
            userClient.getEleveById(eleveId);
        } catch (FeignException e) {
            throw new com.example.exception.ResourceNotFoundException("L'eleve avec l'ID: " + eleveId + " n'existe pas");
        }
    }

    private void validateEnseignantExists(UUID enseignantId) {
        try {
            userClient.getEnseignantById(enseignantId);
        } catch (FeignException e) {
            throw new com.example.exception.ResourceNotFoundException("L'enseignant avec l'ID: " + enseignantId + " n'existe pas");
        }
    }

    private void validateMatiereExists(UUID matiereId) {
        try {
            matiereClient.getMatiereById(matiereId);
        } catch (FeignException e) {
            throw new com.example.exception.ResourceNotFoundException("La matiere avec l'ID: " + matiereId + " n'existe pas");
        }
    }

    private void validateClasseExists(UUID classeId) {
        try {
            userClient.getStudentsByClasseId(classeId);
        } catch (FeignException e) {
            throw new com.example.exception.ResourceNotFoundException("La classe avec l'ID: " + classeId + " n'existe pas");
        }
    }

    @Override
    public void deleteNote(UUID id) {
        Note n = noteRepository.findById(id).orElseThrow(() -> new com.example.exception.ResourceNotFoundException("Note non trouvée: " + id));
        noteRepository.delete(n);
    }

    private NoteResponseDto toDto(Note n) {
        NoteResponseDto dto = new NoteResponseDto();
        dto.setId(n.getId());
        dto.setEleveId(n.getEleveId());
        dto.setMatiereId(n.getMatiereId());
        dto.setClasseId(n.getClasseId());
        dto.setEnseignantId(n.getEnseignantId());
        dto.setNumeroExamen(n.getNumeroExamen());
        dto.setTypeNote(n.getTypeNote());
        dto.setValeur(n.getValeur());
        dto.setCommentaire(n.getCommentaire());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }
}
