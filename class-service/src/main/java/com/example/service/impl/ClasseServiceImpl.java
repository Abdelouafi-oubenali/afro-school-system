package com.example.service.impl;

import com.example.dto.ClasseRequestDto;
import com.example.dto.ClasseResponseDto;
import com.example.dto.EleveResponseDto;
import com.example.dto.EnseignantResponseDto;
import com.example.entity.Classe;
import com.example.mapper.ClasseMapper;
import com.example.repository.ClasseRepository;
import com.example.service.ClasseService;
import com.example.service.UserClient;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ClasseServiceImpl implements ClasseService {


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
            .map(classe -> {
                ClasseResponseDto dto = classeMapper.toDto(classe);
                dto.setEleves(userClient.getStudentsByClasseId(classe.getId()));
                return dto;
            })
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
        classeRepository.findById(classeId)
                .orElseThrow(() -> new RuntimeException("Classe introuvable avec l'id : " + classeId));

        userClient.assignClasseToStudent(studentId, classeId);
    }

    @Override
    public void assignStudentsToClasse(UUID classeId, List<UUID> studentIds) {
        classeRepository.findById(classeId)
                .orElseThrow(() -> new RuntimeException("Classe introuvable avec l'id : " + classeId));

        if (studentIds == null || studentIds.isEmpty()) {
            throw new RuntimeException("La liste des eleves ne doit pas etre vide");
        }

        studentIds.forEach(studentId -> userClient.assignClasseToStudent(studentId, classeId));
    }

    @Override
    public List<EleveResponseDto> getStudentsByClasseId(UUID classeId) {
        if (!classeRepository.existsById(classeId)) {
            throw new RuntimeException("Classe introuvable avec l'id : " + classeId);
        }
        return userClient.getStudentsByClasseId(classeId);
    }

    @Override
    public void assignEnseignantToClasse(UUID classeId, UUID enseignantId) {
        if(!classeRepository.existsById(classeId)) {
            throw new RuntimeException("Classe introuvable avec l'id : " + classeId);
        }
        userClient.getEnseignantById(enseignantId);
        userClient.assignEnseignantToClasse(enseignantId, classeId);
    }

    @Override
    public List<EnseignantResponseDto> getEnseignantsByClasseId(UUID classeId) {
        if(!classeRepository.existsById(classeId)) {
            throw new RuntimeException("Classe introuvable avec l'id : " + classeId);
        }
        return userClient.getEnseignantsByClasseId(classeId);
    }       
}