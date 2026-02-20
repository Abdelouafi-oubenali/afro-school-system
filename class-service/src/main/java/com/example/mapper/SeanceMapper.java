package com.example.mapper;

import com.example.dto.SeanceRequestDto;
import com.example.dto.SeanceResponseDto;
import com.example.entity.Seance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeanceMapper {

    @Mapping(source = "classe.id", target = "classeId")
    @Mapping(source = "classe.name", target = "classeNom")
    @Mapping(source = "matiere.id", target = "matiereId")
    @Mapping(source = "matiere.nom", target = "matiereNom")
    @Mapping(source = "enseignant", target = "enseignantId")
    SeanceResponseDto toDto(Seance seance);

    Seance toEntity(SeanceRequestDto seanceRequestDto);
}
