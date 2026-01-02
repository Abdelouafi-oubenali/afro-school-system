package org.example.userservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@DiscriminatorValue("ELEVE")
@Data
public class Eleve extends User {

    private Long classe;
}

