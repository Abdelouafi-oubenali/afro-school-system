package com.example.mapper;

import com.example.dto.ClasseRequestDto;
import com.example.dto.ClasseResponseDto;
import com.example.entity.Classe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClasseMapper {

    ClasseMapper INSTANCE = Mappers.getMapper(ClasseMapper.class);

    Classe toEntity(ClasseRequestDto dto);

    ClasseResponseDto toDto(Classe classe);
}
