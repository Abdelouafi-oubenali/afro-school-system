package com.example.repository;

import com.example.entity.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere, UUID> {
    Optional<Matiere> findByNom(String nom);
}
