package org.example.userservice.repository;

import org.example.userservice.entity.Admin;
import org.example.userservice.entity.Enseignant;
import org.example.userservice.entity.User;
import org.example.userservice.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.xml.stream.events.EndDocument;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface EnseignantRepository extends JpaRepository<Enseignant, UUID> {
    Optional<Enseignant> findByEmail(String email);
    List<Enseignant> findBySpecialite(String spesialte) ;
    Page<Enseignant> findAllByRole(Role role , Pageable pageable) ;
    Optional<Enseignant> findByIdAndRole(UUID id , Role role) ;
    List<Enseignant> findByNomContainingOrPrenomContainingAndRole(String nom, String prenom, Role role);
    

    boolean existsByMatricule(String matricule);
}
