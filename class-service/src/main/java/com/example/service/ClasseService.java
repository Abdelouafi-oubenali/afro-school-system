package com.example.service;

import com.example.dto.ClasseRequestDto;
import com.example.dto.ClasseResponseDto;
import com.example.dto.EleveResponseDto;
import com.example.dto.EnseignantResponseDto;
import java.util.List;
import java.util.UUID;

public interface ClasseService {

    ClasseResponseDto create(ClasseRequestDto classe);

    ClasseResponseDto update(UUID id, ClasseRequestDto classe);

    ClasseResponseDto getById(UUID id);

    List<ClasseResponseDto> getAll();

    void delete(UUID id);

    void assignStudentToClasse(UUID classeId, UUID studentId);
    void assignStudentsToClasse(UUID classeId, List<UUID> studentIds);
    void assignEnseignantToClasse(UUID classeId, UUID enseignantId);    

    List<EleveResponseDto> getStudentsByClasseId(UUID classeId);
    List<EnseignantResponseDto> getEnseignantsByClasseId(UUID classeId);
    List<ClasseResponseDto> getClassesByEnseignantId(UUID enseignantId);
    
}
