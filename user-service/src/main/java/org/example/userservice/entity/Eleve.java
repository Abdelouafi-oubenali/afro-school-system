package org.example.userservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("ELEVE")
public class Eleve extends User {

    private Long classe;
}

