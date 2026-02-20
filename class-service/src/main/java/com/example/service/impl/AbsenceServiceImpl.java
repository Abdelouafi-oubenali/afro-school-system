package com.example.service.impl;

import com.example.dto.AbsenceRequestDto;
import com.example.dto.AbsenceBulkRequestDto;
import com.example.dto.AbsenceResponseDto;
import com.example.dto.SeanceResponseDto;
import com.example.entity.Absence;
import com.example.entity.Seance;
import com.example.mapper.AbsenceMapper;
import com.example.repository.AbsenceRepository;
import com.example.service.AbsenceService;
import com.example.service.UserClient;
import com.example.service.MatiereService;
import com.example.service.SeanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.service.ClasseService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final AbsenceMapper absenceMapper;
    private final UserClient userClient;
    private final MatiereService matiereService;
    private final SeanceService seanceService;
    private final ClasseService classeService;

    public AbsenceServiceImpl(AbsenceRepository absenceRepository, AbsenceMapper absenceMapper,
                             UserClient userClient, MatiereService matiereService, SeanceService seanceService, ClasseService classeService) {
        this.absenceRepository = absenceRepository;
        this.absenceMapper = absenceMapper;
        this.userClient = userClient;
        this.matiereService = matiereService;
        this.seanceService = seanceService;
        this.classeService = classeService;
    }

    @Override
    public AbsenceResponseDto createAbsence(AbsenceRequestDto absenceRequestDto) {
        validateEleveExists(absenceRequestDto.getEleveId());
        
        validateEnseignantExists(absenceRequestDto.getEnseignantId());
        
        validateSeanceExists(absenceRequestDto.getSeanceId());

        UUID id = absenceRequestDto.getSeanceId();

        SeanceResponseDto seance = seanceService.getSeanceById(id)  ;

        if (!seance.getEnseignantId().equals(absenceRequestDto.getEnseignantId())) {
            throw new RuntimeException("L'enseignant spécifié n'est pas celui de la séance");
        }

        validateClasseExists(absenceRequestDto.getClasseId());
        validateSeanceDateAndTime(seance, absenceRequestDto);
        
        Absence absence = absenceMapper.toEntity(absenceRequestDto);
        Absence savedAbsence = absenceRepository.save(absence);
        return absenceMapper.toDto(savedAbsence);
    }

    @Override
    public List<AbsenceResponseDto> createAbsencesBulk(AbsenceBulkRequestDto absenceBulkRequestDto) {
        validateEnseignantExists(absenceBulkRequestDto.getEnseignantId());
        
        validateSeanceExists(absenceBulkRequestDto.getSeanceId());
        
        validateClasseExists(absenceBulkRequestDto.getClasseId());
        
        UUID seanceId = absenceBulkRequestDto.getSeanceId();
        SeanceResponseDto seance = seanceService.getSeanceById(seanceId);
        
        if (!seance.getEnseignantId().equals(absenceBulkRequestDto.getEnseignantId())) {
            throw new RuntimeException("L'enseignant spécifié n'est pas celui de la séance");
        }
        
        // Vérifier que la date et l'heure de la séance correspondent
        validateSeanceDateAndTimeBulk(seance, absenceBulkRequestDto);
        
        List<Absence> absences = absenceBulkRequestDto.getEleveIds()
                .stream()
                .map(eleveId -> {
                    Absence absence = new Absence();
                    absence.setEleve(eleveId);
                    absence.setEnseignant(absenceBulkRequestDto.getEnseignantId());
                    absence.setSeance(absenceBulkRequestDto.getSeanceId());
                    absence.setClasse(absenceBulkRequestDto.getClasseId());
                    absence.setDate(absenceBulkRequestDto.getDate());
                    absence.setHeureDebut(absenceBulkRequestDto.getHeureDebut());
                    absence.setHeureFin(absenceBulkRequestDto.getHeureFin());
                    absence.setType(absenceBulkRequestDto.getType());
                    absence.setMotif(absenceBulkRequestDto.getMotif());
                    return absence;
                })
                .toList();

        List<Absence> savedAbsences = absenceRepository.saveAll(absences);
        return savedAbsences.stream()
                .map(absenceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AbsenceResponseDto getAbsenceById(UUID id) {
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Absence non trouvée avec l'ID: " + id));
        return absenceMapper.toDto(absence);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsenceResponseDto> getAllAbsences() {
        return absenceRepository.findAll()
                .stream()
                .map(absenceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsenceResponseDto> getAbsencesByEleve(UUID eleveId) {
        return absenceRepository.findByEleve(eleveId)
                .stream()
                .map(absenceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsenceResponseDto> getAbsencesByEnseignant(UUID enseignantId) {
        return absenceRepository.findByEnseignant(enseignantId)
                .stream()
                .map(absenceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsenceResponseDto> getAbsencesByClasse(UUID classeId) {
        return absenceRepository.findByClasse(classeId)
                .stream()
                .map(absenceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsenceResponseDto> getAbsencesByDate(LocalDate date) {
        return absenceRepository.findByDate(date)
                .stream()
                .map(absenceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsenceResponseDto> getAbsencesByEleveAndDate(UUID eleveId, LocalDate date) {
        return absenceRepository.findByEleveAndDate(eleveId, date)
                .stream()
                .map(absenceMapper::toDto)
                .toList();
    }

    @Override
    public AbsenceResponseDto updateAbsence(UUID id, AbsenceRequestDto absenceRequestDto) {
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Absence non trouvée avec l'ID: " + id));

        if (absenceRequestDto.getEleveId() != null) {
            // Valider que le nouvel élève existe
            validateEleveExists(absenceRequestDto.getEleveId());
            absence.setEleve(absenceRequestDto.getEleveId());
        }

        if (absenceRequestDto.getEnseignantId() != null) {
            // Valider que le nouvel enseignant existe
            validateEnseignantExists(absenceRequestDto.getEnseignantId());
            absence.setEnseignant(absenceRequestDto.getEnseignantId());
        }

        if (absenceRequestDto.getClasseId() != null) {
            validateClasseExists(absenceRequestDto.getClasseId());
            absence.setClasse(absenceRequestDto.getClasseId());
        }

        if (absenceRequestDto.getSeanceId() != null) {
            validateSeanceExists(absenceRequestDto.getSeanceId());
            
            UUID seanceId = absenceRequestDto.getSeanceId();
            SeanceResponseDto seance = seanceService.getSeanceById(seanceId);
            
            UUID enseignantIdToCheck = absenceRequestDto.getEnseignantId() != null 
                ? absenceRequestDto.getEnseignantId() 
                : absence.getEnseignant();
            
            if (!seance.getEnseignantId().equals(enseignantIdToCheck)) {
                throw new RuntimeException("L'enseignant spécifié n'est pas celui de la séance");
            }
            
            // Valider la date et l'heure de la séance
            validateSeanceDateAndTimeUpdate(seance, absenceRequestDto, absence);
            
            absence.setSeance(absenceRequestDto.getSeanceId());
        }

        if (absenceRequestDto.getDate() != null) {
            absence.setDate(absenceRequestDto.getDate());
        }

        if (absenceRequestDto.getHeureDebut() != null) {
            absence.setHeureDebut(absenceRequestDto.getHeureDebut());
        }

        if (absenceRequestDto.getHeureFin() != null) {
            absence.setHeureFin(absenceRequestDto.getHeureFin());
        }

        if (absenceRequestDto.getType() != null) {
            absence.setType(absenceRequestDto.getType());
        }

        if (absenceRequestDto.getMotif() != null) {
            absence.setMotif(absenceRequestDto.getMotif());
        }

        Absence updatedAbsence = absenceRepository.save(absence);
        return absenceMapper.toDto(updatedAbsence);
    }

    @Override
    public void deleteAbsence(UUID id) {
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Absence non trouvée avec l'ID: " + id));
        absenceRepository.delete(absence);
    }

    /**
     * Valide que l'élève existe via le service utilisateur
     * @param eleveId l'ID de l'élève à valider
     * @throws RuntimeException si l'élève n'existe pas
     */
    private void validateEleveExists(UUID eleveId) {
        try {
            userClient.getClasseIdByStudent(eleveId);
        } catch (Exception e) {
            throw new RuntimeException("L'élève avec l'ID: " + eleveId + " n'existe pas");
        }
    }

    /**
     * Valide que l'enseignant existe via le service utilisateur
     * @param enseignantId l'ID de l'enseignant à valider
     * @throws RuntimeException si l'enseignant n'existe pas
     */
    private void validateEnseignantExists(UUID enseignantId) {
        try {
            userClient.getEnseignantById(enseignantId);
        } catch (Exception e) {
            throw new RuntimeException("L'enseignant avec l'ID: " + enseignantId + " n'existe pas");
        }
    }

    /**
     * Valide qu'une matière (science) existe
     * @param matiereId l'ID de la matière à valider
     * @throws RuntimeException si la matière n'existe pas
     */
    private void validateMatiereExists(UUID matiereId) {
        try {
            matiereService.getMatiereById(matiereId);
        } catch (Exception e) {
            throw new RuntimeException("La matière avec l'ID: " + matiereId + " n'existe pas");
        }
    }

    /**
     * Valide qu'une séance existe
     * @param seanceId l'ID de la séance à valider
     * @throws RuntimeException si la séance n'existe pas
     */
    private void validateSeanceExists(UUID seanceId) {
        try {
            seanceService.getSeanceById(seanceId);
        } catch (Exception e) {
            throw new RuntimeException("La séance avec l'ID: " + seanceId + " n'existe pas");
        }
    }

    /**
     * Valide qu'une classe existe
     * @param classeId l'ID de la classe à valider
     * @throws RuntimeException si la classe n'existe pas
     */
    private void validateClasseExists(UUID classeId) {
        try {
            classeService.getById(classeId);
        } catch (Exception e) {
            throw new RuntimeException("La classe avec l'ID: " + classeId + " n'existe pas");
        }
    }

    /**
     * Valide que la date et l'heure de l'absence correspondent à celles de la séance
     * @param seance la séance
     * @param absenceRequestDto la requête d'absence
     * @throws RuntimeException si la date ou l'heure ne correspondent pas
     */
    private void validateSeanceDateAndTime(SeanceResponseDto seance, AbsenceRequestDto absenceRequestDto) {
       
        
        if (seance.getHeureDebut() != null && absenceRequestDto.getHeureDebut() != null) {
            if (!seance.getHeureDebut().equals(absenceRequestDto.getHeureDebut())) {
                throw new RuntimeException("L'heure de début de l'absence ne correspond pas à celle de la séance");
            }
        }
        
        if (seance.getHeureFin() != null && absenceRequestDto.getHeureFin() != null) {
            if (!seance.getHeureFin().equals(absenceRequestDto.getHeureFin())) {
                throw new RuntimeException("L'heure de fin de l'absence ne correspond pas à celle de la séance");
            }
        }
    }

    /**
     * Valide que la date et l'heure de l'absence correspondent à celles de la séance (pour bulk)
     * @param seance la séance
     * @param absenceBulkRequestDto la requête d'absence en masse
     * @throws RuntimeException si la date ou l'heure ne correspondent pas
     */
    private void validateSeanceDateAndTimeBulk(SeanceResponseDto seance, AbsenceBulkRequestDto absenceBulkRequestDto) {
       
        
        if (seance.getHeureDebut() != null && absenceBulkRequestDto.getHeureDebut() != null) {
            if (!seance.getHeureDebut().equals(absenceBulkRequestDto.getHeureDebut())) {
                throw new RuntimeException("L'heure de début de l'absence ne correspond pas à celle de la séance");
            }
        }
        
        if (seance.getHeureFin() != null && absenceBulkRequestDto.getHeureFin() != null) {
            if (!seance.getHeureFin().equals(absenceBulkRequestDto.getHeureFin())) {
                throw new RuntimeException("L'heure de fin de l'absence ne correspond pas à celle de la séance");
            }
        }
    }

    /**
     * Valide que la date et l'heure de l'absence correspondent à celles de la séance (pour update)
     * @param seance la séance
     * @param absenceRequestDto la requête d'absence
     * @param absence l'absence actuelle
     * @throws RuntimeException si la date ou l'heure ne correspondent pas
     */
    private void validateSeanceDateAndTimeUpdate(SeanceResponseDto seance, AbsenceRequestDto absenceRequestDto, Absence absence) {
       
        
        LocalTime heureDebutToCheck = absenceRequestDto.getHeureDebut() != null 
            ? absenceRequestDto.getHeureDebut() 
            : absence.getHeureDebut();
        
        if (seance.getHeureDebut() != null && heureDebutToCheck != null) {
            if (!seance.getHeureDebut().equals(heureDebutToCheck)) {
                throw new RuntimeException("L'heure de début de l'absence ne correspond pas à celle de la séance");
            }
        }
        
        LocalTime heureFinToCheck = absenceRequestDto.getHeureFin() != null 
            ? absenceRequestDto.getHeureFin() 
            : absence.getHeureFin();
        
        if (seance.getHeureFin() != null && heureFinToCheck != null) {
            if (!seance.getHeureFin().equals(heureFinToCheck)) {
                throw new RuntimeException("L'heure de fin de l'absence ne correspond pas à celle de la séance");
            }
        }
    }
}
