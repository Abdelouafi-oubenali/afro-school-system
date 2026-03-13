package com.example.service.impl;

import com.example.client.MatiereClient;
import com.example.client.UserClient;
import com.example.dto.BilanMoyenneResponseDto;
import com.example.dto.MatiereResponseDto;
import com.example.dto.MoyenneMatiereDto;
import com.example.dto.NoteRequestDto;
import com.example.dto.NoteResponseDto;
import com.example.entity.Note;
import com.example.entity.NoteType;
import com.example.repository.NoteRepository;
import com.example.service.NoteService;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public BilanMoyenneResponseDto calculateBilanMoyenne(UUID eleveId) {
        validateEleveExists(eleveId);

        List<Note> notes = noteRepository.findByEleveId(eleveId);
        if (notes.isEmpty()) {
            throw new com.example.exception.ResourceNotFoundException("Aucune note trouvee pour l'eleve: " + eleveId);
        }

        Map<UUID, List<Note>> notesParMatiere = notes.stream()
                .filter(note -> note.getMatiereId() != null)
                .collect(Collectors.groupingBy(Note::getMatiereId));

        if (notesParMatiere.isEmpty()) {
            throw new com.example.exception.ResourceNotFoundException("Aucune note liee a une matiere pour l'eleve: " + eleveId);
        }

        List<MoyenneMatiereDto> detailsParMatiere = new ArrayList<>();
        double sommePonderee = 0.0;
        double sommeCoefficients = 0.0;

        for (Map.Entry<UUID, List<Note>> entry : notesParMatiere.entrySet()) {
            UUID matiereId = entry.getKey();
            List<Note> notesMatiere = entry.getValue();

            Double moyenneDevoir = moyenneParType(notesMatiere, NoteType.DEVOIR);
            Double moyenneExamen = moyenneParType(notesMatiere, NoteType.EXAMEN);
            Double moyenneFinale = calculMoyenneFinaleMatiere(moyenneDevoir, moyenneExamen);

            if (moyenneFinale == null) {
                continue;
            }

            MatiereResponseDto matiere = matiereClient.getMatiereById(matiereId);
            double coefficient = extractCoefficient(matiere);

            sommePonderee += moyenneFinale * coefficient;
            sommeCoefficients += coefficient;

            detailsParMatiere.add(new MoyenneMatiereDto(
                    matiereId,
                    matiere != null ? matiere.getNom() : null,
                    round2(coefficient),
                    moyenneDevoir != null ? round2(moyenneDevoir) : null,
                    moyenneExamen != null ? round2(moyenneExamen) : null,
                    round2(moyenneFinale)
            ));
        }

        if (sommeCoefficients == 0.0) {
            throw new com.example.exception.ResourceNotFoundException("Impossible de calculer la moyenne: aucun coefficient exploitable");
        }

        double moyenneGenerale = sommePonderee / sommeCoefficients;
        return new BilanMoyenneResponseDto(
                eleveId,
                round2(moyenneGenerale),
                round2(sommeCoefficients),
                detailsParMatiere.size(),
                detailsParMatiere
        );
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
            MatiereResponseDto matiere = matiereClient.getMatiereById(matiereId);
            if (matiere == null || matiere.getId() == null) {
                throw new com.example.exception.ResourceNotFoundException("La matiere avec l'ID: " + matiereId + " n'existe pas");
            }
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

    private Double moyenneParType(List<Note> notes, NoteType type) {
        return notes.stream()
                .filter(note -> type.equals(note.getTypeNote()))
                .map(Note::getValeur)
                .filter(valeur -> valeur != null)
                .mapToDouble(Double::doubleValue)
                .average()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);
    }

    private Double calculMoyenneFinaleMatiere(Double moyenneDevoir, Double moyenneExamen) {
        if (moyenneDevoir == null && moyenneExamen == null) {
            return null;
        }
        if (moyenneDevoir == null) {
            return moyenneExamen;
        }
        if (moyenneExamen == null) {
            return moyenneDevoir;
        }
        return (moyenneDevoir + moyenneExamen) / 2.0;
    }

    private double extractCoefficient(MatiereResponseDto matiere) {
        if (matiere == null || matiere.getCoefficient() == null || matiere.getCoefficient() <= 0) {
            return 1.0;
        }
        return matiere.getCoefficient();
    }

    private double round2(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
