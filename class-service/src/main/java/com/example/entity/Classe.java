package com.example.entity;

import com.example.enums.LevelClasse;
import com.example.enums.NiveauScolaire;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "classe")
@Data
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "enseignant_principal_id")
    private UUID enseignantPrincipal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LevelClasse levelClasse;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_scolaire")
    private NiveauScolaire niveauScolaire;

    @Column(name = "annee_scolaire")
    private String anneeScolaire;


}