package org.example.userservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@DiscriminatorValue("ELEVE")
@Data
public class Eleve extends User {

    private UUID classe;

}

