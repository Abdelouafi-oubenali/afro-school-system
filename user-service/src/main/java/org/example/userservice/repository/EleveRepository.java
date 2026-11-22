package org.example.userservice.repository;

import org.example.userservice.entity.Eleve;
import org.example.userservice.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EleveRepository extends JpaRepository<Eleve, UUID> {
    //Optional<Eleve> FindBy(String matricule);

    Optional<Eleve> findByIdAndRole(UUID id, Role role);
    Optional<Eleve> findByEmailAndRole(String email, Role role);
    List<Eleve> findAllByRole(Role role);
    Page<Eleve> findAllByRole(Role role, Pageable pageable);
    List<Eleve> findByNomContainingOrPrenomContainingAndRole(String str , String str2 , Role role) ;
    List<Eleve> findAllByClasseId(UUID classeId);


}
