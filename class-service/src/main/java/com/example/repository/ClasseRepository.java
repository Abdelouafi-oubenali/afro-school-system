package com.example.repository;

import com.example.entity.Classe;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClasseRepository extends JpaRepository<Classe, UUID> {
	Optional<Classe> findByName(String name);
}

