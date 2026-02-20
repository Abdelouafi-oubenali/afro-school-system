package com.example.mapper;

import com.example.dto.AbsenceRequestDto;
import com.example.dto.AbsenceResponseDto;
import com.example.entity.Absence;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AbsenceMapper {

    @Mapping(source = "eleve", target = "eleveId")
    @Mapping(source = "enseignant", target = "enseignantId")
    @Mapping(source = "seance", target = "seanceId")
    @Mapping(source = "classe", target = "classeId")
    AbsenceResponseDto toDto(Absence absence);

    @Mapping(source = "eleveId", target = "eleve")
    @Mapping(source = "enseignantId", target = "enseignant")
    @Mapping(source = "seanceId", target = "seance")
    @Mapping(source = "classeId", target = "classe")
    Absence toEntity(AbsenceRequestDto absenceRequestDto);
}
