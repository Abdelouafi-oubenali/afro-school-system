package org.example.userservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@DiscriminatorValue("PARENT")
@Data
public class Parent extends User {
    private List<UUID> childIds;
}
