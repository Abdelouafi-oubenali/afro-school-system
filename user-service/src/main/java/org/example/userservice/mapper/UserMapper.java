package org.example.userservice.mapper;

import org.example.userservice.dto.UserDTO;
import org.example.userservice.entity.*;
import org.example.userservice.enums.Role;
import org.mapstruct.Mapper;

import static org.example.userservice.enums.Role.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default User toEntity(UserDTO dto) {
        User user;
        switch (dto.getRole()) {
            case ADMIN -> {
                Admin admin = new Admin();
                admin.setMatricule(dto.getMatricule());
                user = admin;
            }
            case ELEVE -> {
                Eleve eleve = new Eleve();
                eleve.setClasse(dto.getClasse());
                user = eleve;
            }
            case PARENT -> {
                Parent parent = new Parent();
                parent.setChildIds(dto.getChildIds());
                user = parent;
            }
            case ENSEIGNANT -> {
                Enseignant enseignant = new Enseignant();
                enseignant.setMatricule(dto.getMatricule());
                enseignant.setSpecialite(dto.getSpecialite());
                enseignant.setDateEmbauche(dto.getDateEmbauche());
                enseignant.setClasses(dto.getClasses());
                user = enseignant;
            }
            default -> throw new IllegalArgumentException("Role non supporté: " + dto.getRole());
        }

        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhone(dto.getPhone());
        user.setDateNaissance(dto.getDateNaissance());
        user.setRole(dto.getRole());
        return user;
    }

    UserDTO toDTO(User user);
}
