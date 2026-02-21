package com.example.repository;

import com.example.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
    List<Note> findByEleveId(UUID eleveId);
    List<Note> findByClasseId(UUID classeId);
    List<Note> findByEleveIdAndMatiereId(UUID eleveId, UUID matiereId);
}
