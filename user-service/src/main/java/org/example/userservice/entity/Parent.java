package org.example.userservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.userservice.entity.Eleve;
import org.example.userservice.entity.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@DiscriminatorValue("PARENT")
@Data
@Table(name = "parent")
@PrimaryKeyJoinColumn(name = "id")
public class Parent extends User {

    @ElementCollection
    @CollectionTable(
            name = "parent_children",
            joinColumns = @JoinColumn(name = "parent_id")
    )
    @Column(name = "child_id")
    private List<UUID> childIds = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "parent_children",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    @Transient
    private List<Eleve> enfants = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}