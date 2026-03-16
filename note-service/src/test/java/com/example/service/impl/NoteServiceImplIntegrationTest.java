package com.example.service.impl;

import com.example.dto.NoteRequestDto;
import com.example.dto.NoteResponseDto;
import com.example.entity.NoteType;
import com.example.repository.NoteRepository;
import com.example.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NoteServiceImplIntegrationTest {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @MockBean
    private com.example.client.UserClient userClient;

    @MockBean
    private com.example.client.MatiereClient matiereClient;

    private UUID eleveId;
    private UUID matiereId;
    private UUID enseignantId;
    private UUID classeId;

    @BeforeEach
    void setUp() {
        eleveId = UUID.randomUUID();
        matiereId = UUID.randomUUID();
        enseignantId = UUID.randomUUID();
        classeId = UUID.randomUUID();
        // Mock user/matiere existence
        // UserClient methods are void, so no thenReturn needed
        // MatiereClient returns a DTO with the correct ID
        com.example.dto.MatiereResponseDto matiereDto = new com.example.dto.MatiereResponseDto();
        matiereDto.setId(matiereId);
        matiereDto.setNom("Math");
        matiereDto.setDescription("Mathématiques");
        matiereDto.setCoefficient(1.0);
        matiereDto.setEstActif(true);
        when(matiereClient.getMatiereById(matiereId)).thenReturn(matiereDto);
    }

    @Test
    void testCreateAndGetNote() {
        NoteRequestDto req = new NoteRequestDto();
        req.setEleveId(eleveId);
        req.setMatiereId(matiereId);
        req.setEnseignantId(enseignantId);
        req.setClasseId(classeId);
        req.setTypeNote(NoteType.DEVOIR);
        req.setNumeroExamen(1);
        req.setValeur(15.5);
        req.setCommentaire("Bien joué");

        NoteResponseDto created = noteService.createNote(req);
        assertThat(created).isNotNull();
        assertThat(created.getValeur()).isEqualTo(15.5);
        assertThat(created.getCommentaire()).isEqualTo("Bien joué");

        NoteResponseDto fetched = noteService.getNoteById(created.getId());
        assertThat(fetched).isNotNull();
        assertThat(fetched.getValeur()).isEqualTo(15.5);
    }

    @Test
    void testGetNotesByEleve() {
        NoteRequestDto req = new NoteRequestDto();
        req.setEleveId(eleveId);
        req.setMatiereId(matiereId);
        req.setEnseignantId(enseignantId);
        req.setClasseId(classeId);
        req.setTypeNote(NoteType.EXAMEN);
        req.setNumeroExamen(2);
        req.setValeur(12.0);
        req.setCommentaire("Correct");
        noteService.createNote(req);

        List<NoteResponseDto> notes = noteService.getNotesByEleve(eleveId);
        assertThat(notes).hasSize(1);
        assertThat(notes.get(0).getTypeNote()).isEqualTo(NoteType.EXAMEN);
    }

    @Test
    void testGetNotesByEleveAndMatiere() {
        NoteRequestDto req = new NoteRequestDto();
        req.setEleveId(eleveId);
        req.setMatiereId(matiereId);
        req.setEnseignantId(enseignantId);
        req.setClasseId(classeId);
        req.setTypeNote(NoteType.EXAMEN);
        req.setNumeroExamen(2);
        req.setValeur(12.0);
        req.setCommentaire("Correct");
        noteService.createNote(req);

        List<NoteResponseDto> notes = noteService.getNotesByEleveAndMatiere(eleveId, matiereId);
        assertThat(notes).hasSize(1);
        assertThat(notes.get(0).getMatiereId()).isEqualTo(matiereId);
    }
}
