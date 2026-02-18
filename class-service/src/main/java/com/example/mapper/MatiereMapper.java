package com.example.mapper;

import com.example.dto.MatiereRequestDto;
import com.example.dto.MatiereResponseDto;
import com.example.entity.Matiere;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MatiereMapper {

    MatiereResponseDto toDto(Matiere matiere);

    Matiere toEntity(MatiereRequestDto matiereRequestDto);
}
