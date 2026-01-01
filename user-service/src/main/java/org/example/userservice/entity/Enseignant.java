package org.example.userservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

@Entity
@DiscriminatorValue("ENSEIGNANT")
public class Enseignant extends User {

    private String matricule;
    private String specialite;
    private LocalDate dateEmbauche;
    private List<Long> classes;
}

