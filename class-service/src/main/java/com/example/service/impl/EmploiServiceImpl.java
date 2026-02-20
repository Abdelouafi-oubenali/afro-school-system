package com.example.service.impl;

import com.example.dto.SeanceResponseDto;
import com.example.entity.Seance;
import com.example.mapper.SeanceMapper;
import com.example.repository.SeanceRepository;
import com.example.service.EmploiService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class EmploiServiceImpl implements EmploiService {

	private final SeanceRepository seanceRepository;
	private final SeanceMapper seanceMapper;

	public EmploiServiceImpl(SeanceRepository seanceRepository, SeanceMapper seanceMapper) {
		this.seanceRepository = seanceRepository;
		this.seanceMapper = seanceMapper;
	}

	@Override
	public List<SeanceResponseDto> getEmploiByClasseId(String classeId) {
		UUID id = UUID.fromString(classeId);
		return seanceRepository.findAll()
				.stream()
				.filter(seance -> seance.getClasse().getId().equals(id))
				.map(seanceMapper::toDto)
				.toList();
	}

	@Override
	public List<SeanceResponseDto> getEmploiByEnseignantId(String enseignantId) {
		UUID id = UUID.fromString(enseignantId);
		return seanceRepository.findAll()
				.stream()
				.filter(seance -> seance.getEnseignant().equals(id))
				.map(seanceMapper::toDto)
				.toList();
	}

	@Override
	public List<SeanceResponseDto> getAllEmploiEnseignants() {
		return seanceRepository.findAll()
				.stream()
				.map(seanceMapper::toDto)
				.toList();
	}

	@Override
	public List<SeanceResponseDto> getAllEmploiClasses() {
		return seanceRepository.findAll()
				.stream()
				.map(seanceMapper::toDto)
				.toList();
	}
}
