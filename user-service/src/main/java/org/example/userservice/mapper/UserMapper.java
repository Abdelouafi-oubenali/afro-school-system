package org.example.userservice.mapper;

import org.example.userservice.dto.AdminResponseDTO;
import org.example.userservice.dto.UserResponseDTO;
import org.example.userservice.entity.*;
import org.example.userservice.enums.Role;
import org.mapstruct.Mapper;

import static org.example.userservice.enums.Role.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Base mapping
    UserResponseDTO toBaseResponse(User user);

    // Admin
    AdminResponseDTO toAdminResponse(Admin admin);

}

