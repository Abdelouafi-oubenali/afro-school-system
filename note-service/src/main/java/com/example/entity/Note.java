package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "note")
@Data
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "eleve_id", nullable = false)
    private UUID eleveId;

    @Column(name = "matiere_id")
    private UUID matiereId;

    @Column(name = "enseignant_id")
    private UUID enseignantId;

    @Column(name = "classe_id")
    private UUID classeId;

    @Column(nullable = false)
    private Double valeur;

    @Column(name = "numero_examen")
    private Integer numeroExamen;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_note")
    private NoteType typeNote;

    private String commentaire;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
