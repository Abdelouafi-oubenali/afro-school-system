package org.example.userservice.repository;

import org.example.userservice.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParentRepository extends JpaRepository<Parent , UUID>
{
    Optional<Parent> findByEmail(String email);
    List<Parent> findByChildIdsContaining(UUID childId);
    boolean existsByEmail(String email);
}
