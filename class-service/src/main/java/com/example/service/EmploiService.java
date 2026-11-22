package com.example.service;

import java.util.List;

import com.example.dto.SeanceResponseDto;

public interface EmploiService {

    List<SeanceResponseDto> getEmploiByClasseId(String classeId);
    List<SeanceResponseDto> getEmploiByEnseignantId(String enseignantId);

    List<SeanceResponseDto> getAllEmploiEnseignants();
    List<SeanceResponseDto> getAllEmploiClasses();
    
    
}
