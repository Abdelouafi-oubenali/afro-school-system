package com.example.repository;

import com.example.entity.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, UUID> {
    List<Absence> findByEleve(UUID eleveId);
    List<Absence> findByEnseignant(UUID enseignantId);
    List<Absence> findByClasse(UUID classeId);
    List<Absence> findByDate(LocalDate date);
    List<Absence> findByEleveAndDate(UUID eleveId, LocalDate date);
}
