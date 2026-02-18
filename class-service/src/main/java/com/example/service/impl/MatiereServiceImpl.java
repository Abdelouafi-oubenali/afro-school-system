package com.example.service.impl;

import com.example.dto.MatiereRequestDto;
import com.example.dto.MatiereResponseDto;
import com.example.entity.Matiere;
import com.example.mapper.MatiereMapper;
import com.example.repository.MatiereRepository;
import com.example.service.MatiereService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MatiereServiceImpl implements MatiereService {

    private final MatiereRepository matiereRepository;
    private final MatiereMapper matiereMapper;

    public MatiereServiceImpl(MatiereRepository matiereRepository, MatiereMapper matiereMapper) {
        this.matiereRepository = matiereRepository;
        this.matiereMapper = matiereMapper;
    }

    @Override
    public MatiereResponseDto createMatiere(MatiereRequestDto matiereRequestDto) {
        Matiere matiere = matiereMapper.toEntity(matiereRequestDto);
        matiere.setEstActif(true);
        Matiere savedMatiere = matiereRepository.save(matiere);
        return matiereMapper.toDto(savedMatiere);
    }

    @Override
    @Transactional(readOnly = true)
    public MatiereResponseDto getMatiereById(UUID id) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée avec l'ID: " + id));
        return matiereMapper.toDto(matiere);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatiereResponseDto> getAllMatieres() {
        return matiereRepository.findAll()
                .stream()
                .map(matiereMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatiereResponseDto> getAllMatieresActives() {
        return matiereRepository.findAll()
                .stream()
                .filter(Matiere::isEstActif)
                .map(matiereMapper::toDto)
                .toList();
    }

    @Override
    public MatiereResponseDto updateMatiere(UUID id, MatiereRequestDto matiereRequestDto) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée avec l'ID: " + id));

        if (matiereRequestDto.getNom() != null) {
            matiere.setNom(matiereRequestDto.getNom());
        }
        if (matiereRequestDto.getDescription() != null) {
            matiere.setDescription(matiereRequestDto.getDescription());
        }
        if (matiereRequestDto.getCoefficient() != null) {
            matiere.setCoefficient(matiereRequestDto.getCoefficient());
        }

        Matiere updatedMatiere = matiereRepository.save(matiere);
        return matiereMapper.toDto(updatedMatiere);
    }

    @Override
    public void deleteMatiere(UUID id) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée avec l'ID: " + id));
        matiereRepository.delete(matiere);
    }

    @Override
    public void deactivateMatiere(UUID id) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée avec l'ID: " + id));
        matiere.setEstActif(false);
        matiereRepository.save(matiere);
    }

    @Override
    @Transactional(readOnly = true)
    public MatiereResponseDto getMatiereByNom(String nom) {
        Matiere matiere = matiereRepository.findByNom(nom)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée avec le nom: " + nom));
        return matiereMapper.toDto(matiere);
    }
}
