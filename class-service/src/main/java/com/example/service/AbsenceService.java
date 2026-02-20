package com.example.service;

import com.example.dto.AbsenceRequestDto;
import com.example.dto.AbsenceBulkRequestDto;
import com.example.dto.AbsenceResponseDto;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AbsenceService {

    AbsenceResponseDto createAbsence(AbsenceRequestDto absenceRequestDto);

    List<AbsenceResponseDto> createAbsencesBulk(AbsenceBulkRequestDto absenceBulkRequestDto);

    AbsenceResponseDto getAbsenceById(UUID id);

    List<AbsenceResponseDto> getAllAbsences();

    List<AbsenceResponseDto> getAbsencesByEleve(UUID eleveId);

    List<AbsenceResponseDto> getAbsencesByEnseignant(UUID enseignantId);

    List<AbsenceResponseDto> getAbsencesByDate(LocalDate date);

    List<AbsenceResponseDto> getAbsencesByEleveAndDate(UUID eleveId, LocalDate date);

    AbsenceResponseDto updateAbsence(UUID id, AbsenceRequestDto absenceRequestDto);

    void deleteAbsence(UUID id);
}
