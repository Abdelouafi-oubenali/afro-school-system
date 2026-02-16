package com.example.service.impl;

import com.example.dto.ClasseRequestDto;
import com.example.dto.ClasseResponseDto;
import com.example.entity.Classe;
import com.example.mapper.ClasseMapper;
import com.example.repository.ClasseRepository;
import com.example.service.ClasseService;
import com.example.service.UserClient;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ClasseServiceImpl implements ClasseService , UserClient {


    private final ClasseRepository classeRepository ;
    private final ClasseMapper classeMapper ; 
    private final UserClient userClient ;

    public ClasseServiceImpl(ClasseRepository classeRepository , ClasseMapper classeMapper , UserClient userClient)
    {
        this.classeRepository = classeRepository ;
        this.classeMapper = classeMapper ; 
        this.userClient = userClient ;
    }


    @Override
    public ClasseResponseDto create(ClasseRequestDto dto)
    {
        Classe classe = classeMapper.toEntity(dto);

        classeRepository.findByName(classe.getName())
                .ifPresent(existing -> {
                    throw new RuntimeException("Une classe avec ce nom existe déjà : " + existing.getName());
                });

        Classe saved = classeRepository.save(classe);
        return  classeMapper.toDto(saved) ;
    }

    @Override
    public ClasseResponseDto update(UUID id , ClasseRequestDto dto)
    {
        Classe existing = classeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classe introuvable avec l'id : " + id));

        existing.setName(dto.getName());
        existing.setLevelClasse(dto.getLevelClasse());
        existing.setEnseignantPrincipal(dto.getEnseignantPrincipal());
        existing.setAnneeScolaire(dto.getAnneeScolaire());

        Classe saved = classeRepository.save(existing);
        return classeMapper.toDto(saved);
    }

    @Override
    public  ClasseResponseDto getById (UUID id)
    {
        Classe classe = classeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Classe introuvable avec l'id : " + id));
        return classeMapper.toDto(classe);
    }


    @Override
    public List<ClasseResponseDto> getAll()
    {
        return classeRepository.findAll()
            .stream()
            .map(classeMapper::toDto)
            .toList();
    }

    @Override
    public void delete(UUID id)
    {
        if (!classeRepository.existsById(id)) {
            throw new RuntimeException("Classe introuvable avec l'id : " + id);
        }
        classeRepository.deleteById(id);
    }

    @Override
    public void assignStudentToClasse(UUID classeId, UUID studentId) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new RuntimeException("Classe introuvable avec l'id : " + classeId));

        userClient.assignClasseToStudent(studentId, classeId);
    }

    @Override
    public UUID getClasseIdByStudent(UUID studentId) {
        return userClient.getClasseIdByStudent(studentId);
    }

    @Override
    public void assignClasseToStudent(UUID studentId, UUID classeId) {
        userClient.assignClasseToStudent(studentId, classeId);
    }
}
