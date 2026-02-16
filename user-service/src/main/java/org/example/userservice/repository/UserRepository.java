package org.example.userservice.repository;

import org.example.userservice.entity.Admin;
import org.example.userservice.entity.Eleve;
import org.example.userservice.entity.User;
import org.example.userservice.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.rmi.server.UID;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User , UUID> {
    Optional<User> findByEmail(String email);

    Optional<Admin> findByIdAndRole(UUID id, Role role);

    Optional<Admin> findByEmailAndRole(String email, Role role);

    List<Admin> findAllByRole(Role role);


    Page<Admin> findAllByRole(Role role, Pageable pageable);

}
