package com.example.service;

import com.example.dto.SeanceRequestDto;
import com.example.dto.SeanceResponseDto;
import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public interface SeanceService {
    
    SeanceResponseDto createSeance(SeanceRequestDto seanceRequestDto);

    SeanceResponseDto getSeanceById(UUID id);

    List<SeanceResponseDto> getAllSeances();

    List<SeanceResponseDto> getSeancesByClasse(UUID classeId);

    List<SeanceResponseDto> getSeancesByMatiere(UUID matiereId);

    List<SeanceResponseDto> getSeancesByEnseignant(UUID enseignantId);

    List<SeanceResponseDto> getSeancesByJour(DayOfWeek jour);

    SeanceResponseDto updateSeance(UUID id, SeanceRequestDto seanceRequestDto);

    void deleteSeance(UUID id);

    void deactivateSeance(UUID id);

    List<SeanceResponseDto> getSeancesActives();
}
