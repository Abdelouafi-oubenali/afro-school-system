package com.example.service;

import com.example.dto.MatiereRequestDto;
import com.example.dto.MatiereResponseDto;
import java.util.List;
import java.util.UUID;

public interface MatiereService {

    MatiereResponseDto createMatiere(MatiereRequestDto matiereRequestDto);

    MatiereResponseDto getMatiereById(UUID id);

    List<MatiereResponseDto> getAllMatieres();

    List<MatiereResponseDto> getAllMatieresActives();

    MatiereResponseDto updateMatiere(UUID id, MatiereRequestDto matiereRequestDto);

    void deleteMatiere(UUID id);

    void deactivateMatiere(UUID id);

    MatiereResponseDto getMatiereByNom(String nom);
}
