package com.example.repository;

import com.example.entity.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, UUID> {
}
