package com.example.entity;

import com.example.enums.Type;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "absence")
@Data
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "eleve_id", nullable = false)
    private UUID eleve;

    @Column(name = "enseignant_id", nullable = false)
    private UUID enseignant;

    @Column(name = "seance_id", nullable = false)
    private UUID seance;

    @Column(name = "classe_id", nullable = false)
    private UUID classe;

    @Column(nullable = false)
    private LocalDate date;

    private LocalTime heureDebut;

    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(length = 500)
    private String motif;
}