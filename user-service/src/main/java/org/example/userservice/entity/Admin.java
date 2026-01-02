package org.example.userservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@DiscriminatorValue("ADMIN")
@Data
public class Admin extends User {

    private String matricule;
}
