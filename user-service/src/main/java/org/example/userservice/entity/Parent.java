package org.example.userservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;
import java.util.UUID;

@Entity
@DiscriminatorValue("PARENT")
public class Parent extends User {
    private List<UUID> childIds;
}
