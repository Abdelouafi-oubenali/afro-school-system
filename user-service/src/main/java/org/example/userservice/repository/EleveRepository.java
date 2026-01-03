package org.example.userservice.repository;

import org.example.userservice.entity.Eleve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EleveRepository extends JpaRepository<Eleve, UUID> {
    //Optional<Eleve> FindBy(String matricule);

}
