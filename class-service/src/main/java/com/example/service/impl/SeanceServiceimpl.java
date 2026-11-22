package com.example.service.impl;

import com.example.dto.SeanceRequestDto;
import com.example.dto.SeanceResponseDto;
import com.example.entity.Classe;
import com.example.entity.Matiere;
import com.example.entity.Seance;
import com.example.mapper.SeanceMapper;
import com.example.repository.ClasseRepository;
import com.example.repository.MatiereRepository;
import com.example.repository.SeanceRepository;
import com.example.service.SeanceService;
import com.example.service.UserClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SeanceServiceimpl implements SeanceService {

    private final SeanceRepository seanceRepository;
    private final ClasseRepository classeRepository;
    private final MatiereRepository matiereRepository;
    private final SeanceMapper seanceMapper;
    private final UserClient userClient;

    public SeanceServiceimpl(SeanceRepository seanceRepository, ClasseRepository classeRepository,
                             MatiereRepository matiereRepository, SeanceMapper seanceMapper, UserClient userClient) {
        this.seanceRepository = seanceRepository;
        this.classeRepository = classeRepository;
        this.matiereRepository = matiereRepository;
        this.seanceMapper = seanceMapper;
        this.userClient = userClient;
    }

    @Override
    public SeanceResponseDto createSeance(SeanceRequestDto seanceRequestDto) {
        Classe classe = classeRepository.findById(seanceRequestDto.getClasseId())
                .orElseThrow(() -> new RuntimeException("Classe non trouvée avec l'ID: " + seanceRequestDto.getClasseId()));

        Matiere matiere = matiereRepository.findById(seanceRequestDto.getMatiereId())
                .orElseThrow(() -> new RuntimeException("Matière non trouvée avec l'ID: " + seanceRequestDto.getMatiereId()));

        userClient.getEnseignantById(seanceRequestDto.getEnseignantId());

        if (hasEnseignantSeance(null, seanceRequestDto.getEnseignantId(), seanceRequestDto.getJour(),
                seanceRequestDto.getHeureDebut(), seanceRequestDto.getHeureFin())) {
            throw new RuntimeException("L'enseignant a déjà une séance à cet horaire");
        }

        if (hasClasseSeance(null, seanceRequestDto.getClasseId(), seanceRequestDto.getJour(),
                seanceRequestDto.getHeureDebut(), seanceRequestDto.getHeureFin())) {
            throw new RuntimeException("La classe a déjà une séance à cet horaire");
        }



        Seance seance = new Seance();
        seance.setClasse(classe);
        seance.setMatiere(matiere);
        seance.setEnseignant(seanceRequestDto.getEnseignantId());
        seance.setJour(seanceRequestDto.getJour());
        seance.setHeureDebut(seanceRequestDto.getHeureDebut());
        seance.setHeureFin(seanceRequestDto.getHeureFin());
        seance.setEstActif(true);

        Seance savedSeance = seanceRepository.save(seance);
        return seanceMapper.toDto(savedSeance);
    }

    @Override
    @Transactional(readOnly = true)
    public SeanceResponseDto getSeanceById(UUID id) {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée avec l'ID: " + id));
        return seanceMapper.toDto(seance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeanceResponseDto> getAllSeances() {
        return seanceRepository.findAll()
                .stream()
                .map(seanceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeanceResponseDto> getSeancesByClasse(UUID classeId) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new RuntimeException("Classe non trouvée avec l'ID: " + classeId));

        return seanceRepository.findAll()
                .stream()
                .filter(seance -> seance.getClasse().getId().equals(classeId))
                .map(seanceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeanceResponseDto> getSeancesByMatiere(UUID matiereId) {
        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée avec l'ID: " + matiereId));

        return seanceRepository.findAll()
                .stream()
                .filter(seance -> seance.getMatiere().getId().equals(matiereId))
                .map(seanceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeanceResponseDto> getSeancesByEnseignant(UUID enseignantId) {
        return seanceRepository.findAll()
                .stream()
                .filter(seance -> seance.getEnseignant().equals(enseignantId))
                .map(seanceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeanceResponseDto> getSeancesByJour(DayOfWeek jour) {
        return seanceRepository.findAll()
                .stream()
                .filter(seance -> seance.getJour().equals(jour))
                .map(seanceMapper::toDto)
                .toList();
    }

    @Override
    public SeanceResponseDto updateSeance(UUID id, SeanceRequestDto seanceRequestDto) {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée avec l'ID: " + id));

        if (seanceRequestDto.getClasseId() != null) {
            Classe classe = classeRepository.findById(seanceRequestDto.getClasseId())
                    .orElseThrow(() -> new RuntimeException("Classe non trouvée avec l'ID: " + seanceRequestDto.getClasseId()));
            seance.setClasse(classe);
        }

        if (seanceRequestDto.getMatiereId() != null) {
            Matiere matiere = matiereRepository.findById(seanceRequestDto.getMatiereId())
                    .orElseThrow(() -> new RuntimeException("Matière non trouvée avec l'ID: " + seanceRequestDto.getMatiereId()));
            seance.setMatiere(matiere);
        }

        if (seanceRequestDto.getEnseignantId() != null) {
            // Verify enseignant exists in user-service
            userClient.getEnseignantById(seanceRequestDto.getEnseignantId());
            seance.setEnseignant(seanceRequestDto.getEnseignantId());
        }

        if (seanceRequestDto.getJour() != null) {
            seance.setJour(seanceRequestDto.getJour());
        }

        if (seanceRequestDto.getHeureDebut() != null) {
            seance.setHeureDebut(seanceRequestDto.getHeureDebut());
        }

        if (seanceRequestDto.getHeureFin() != null) {
            seance.setHeureFin(seanceRequestDto.getHeureFin());
        }

        UUID classeId = seance.getClasse().getId();
        UUID enseignantId = seance.getEnseignant();
        DayOfWeek jour = seance.getJour();
        LocalTime heureDebut = seance.getHeureDebut();
        LocalTime heureFin = seance.getHeureFin();

        if (hasEnseignantSeance(id, enseignantId, jour, heureDebut, heureFin)) {
            throw new RuntimeException("L'enseignant a déjà une séance à cet horaire");
        }

        if (hasClasseSeance(id, classeId, jour, heureDebut, heureFin)) {
            throw new RuntimeException("La classe a déjà une séance à cet horaire");
        }

        Seance updatedSeance = seanceRepository.save(seance);
        return seanceMapper.toDto(updatedSeance);
    }

    @Override
    public void deleteSeance(UUID id) {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée avec l'ID: " + id));
        seanceRepository.delete(seance);
    }

    @Override
    public void deactivateSeance(UUID id) {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée avec l'ID: " + id));
        seance.setEstActif(false);
        seanceRepository.save(seance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeanceResponseDto> getSeancesActives() {
        return seanceRepository.findAll()
                .stream()
                .filter(Seance::isEstActif)
                .map(seanceMapper::toDto)
                .toList();
    }

        private boolean hasEnseignantSeance(UUID excludeSeanceId, UUID enseignantId, DayOfWeek jour,
                        LocalTime heureDebut, LocalTime heureFin) {
        return seanceRepository.findAll()
                .stream()
            .filter(seance -> excludeSeanceId == null || !seance.getId().equals(excludeSeanceId))
            .filter(seance -> seance.getEnseignant().equals(enseignantId))
                .anyMatch(seance -> seance.getJour().equals(jour) &&
                ((heureDebut.isBefore(seance.getHeureFin()) && heureFin.isAfter(seance.getHeureDebut())) ||
                    (heureDebut.equals(seance.getHeureDebut()) || heureFin.equals(seance.getHeureFin()))));
    }

        private boolean hasClasseSeance(UUID excludeSeanceId, UUID classeId, DayOfWeek jour,
                        LocalTime heureDebut, LocalTime heureFin) {
        return seanceRepository.findAll()
                .stream()
            .filter(seance -> excludeSeanceId == null || !seance.getId().equals(excludeSeanceId))
            .filter(seance -> seance.getClasse().getId().equals(classeId))
                .anyMatch(seance -> seance.getJour().equals(jour) &&
                ((heureDebut.isBefore(seance.getHeureFin()) && heureFin.isAfter(seance.getHeureDebut())) ||
                    (heureDebut.equals(seance.getHeureDebut()) || heureFin.equals(seance.getHeureFin()))));
    }
}

