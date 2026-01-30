package org.example.userservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@DiscriminatorValue("ELEVE")
@Data
public class Eleve extends User {

    private Long classe;

}

