package com.example.service;

import com.example.dto.ClasseRequestDto;
import com.example.dto.ClasseResponseDto;
import com.example.entity.Classe;
import java.util.List;
import java.util.UUID;

public interface ClasseService {

    ClasseResponseDto create(ClasseRequestDto classe);

    ClasseResponseDto update(UUID id, ClasseRequestDto classe);

    ClasseResponseDto getById(UUID id);

    List<ClasseResponseDto> getAll();

    void delete(UUID id);
}
