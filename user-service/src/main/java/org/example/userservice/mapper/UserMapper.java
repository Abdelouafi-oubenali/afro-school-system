package org.example.userservice.mapper;

import org.example.userservice.dto.*;
import org.example.userservice.entity.*;
import org.example.userservice.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.example.userservice.enums.Role.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Base mapping
    UserResponseDTO toBaseResponse(User user);

    // Admin
    AdminResponseDTO toAdminResponse(Admin admin);

    //enseignant
    EnseignantResponseDTO toEnseigmentResponse(Enseignant enseignant) ;

    //Eleve
    @Mapping(source = "classeId", target = "classe")
    EleveResponseDTO toEleveResponse(Eleve eleve) ;

    //Parent
    ParentResponseDTO toParentResponse(Parent parent) ;

    EnseignantResponseDTO toEnseignantResponse(Enseignant enseignant);
}

