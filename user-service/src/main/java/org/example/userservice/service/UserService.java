package org.example.userservice.service;

import org.example.userservice.dto.*;
import org.example.userservice.entity.Admin;
import org.example.userservice.entity.Eleve;
import org.example.userservice.entity.Enseignant;
import org.example.userservice.entity.User;
import org.example.userservice.enums.Role;
import org.example.userservice.exception.InvalidEnseignantException;
import org.example.userservice.exception.UserAlreadyExistsException;
import org.example.userservice.mapper.UserMapper;
import org.example.userservice.repository.AdminRepository;
import org.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository ;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    public UserDTO createUser(UserDTO dto) {
//
//        if (dto.getRole() == null) {
//            throw new RuntimeException("Role is required");
//        }
//
//        User user;
//
//        switch (dto.getRole()) {
//            case ADMIN -> {
//                Admin admin = new Admin();
//                admin.setMatricule(dto.getMatricule());
//                user = admin;
//            }
//            case ELEVE -> {
//                Eleve eleve = new Eleve();
//                eleve.setClasse(dto.getClasse());
//                user = eleve;
//            }
//            case ENSEIGNANT -> {
//                Enseignant ens = new Enseignant();
//                ens.setSpecialite(dto.getSpecialite());
//                user = ens;
//            }
//            default -> throw new RuntimeException("Unsupported role");
//        }
//
//        user.setId(UUID.randomUUID());
//        user.setNom(dto.getNom());
//        user.setPrenom(dto.getPrenom());
//        user.setEmail(dto.getEmail());
//        user.setPassword(passwordEncoder.encode(dto.getPassword()));
//        user.setRole(dto.getRole());
//
//        User saved = userRepository.save(user);
//        return userMapper.toDTO(saved);
//    }

    //Genirte un new Maricule
    private String generateUniqueMatricule() {
        String matricule;
        do {
            int randomNum = (int)(Math.random() * 9000) + 1000;
            matricule = "ADM" + randomNum;
        } while (adminRepository.existsByMatricule(matricule));
        return matricule;
    }



    // Admin
    public AdminResponseDTO createAdmin(CreateUserRequest dto) {

        if(userRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User already exists with email: " + dto.getEmail());
        }
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        admin.setNom(dto.getNom());
        admin.setPrenom(dto.getPrenom());
        admin.setEmail(dto.getEmail());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setPhone(dto.getPhone());
        admin.setDateNaissance(dto.getDateNaissance());
        admin.setRole(Role.ADMIN);
        admin.setMatricule(generateUniqueMatricule());

        Admin saved = userRepository.save(admin);
        return userMapper.toAdminResponse(saved);
    }


    //ENSEIGNANT
    public EnseignantResponseDTO createEnseignent(CreateUserRequest dto)
    {
        if(userRepository.findByEmail(dto.getEmail()).isPresent())
        {
            throw new UserAlreadyExistsException("User already exists with email: " + dto.getEmail());
        }

        if (dto.getSpecialite() == null || dto.getDateEmbauche() == null) {
            throw new InvalidEnseignantException(
                    "specialite et dateEmbauche sont obligatoires pour un Enseignant"
            );
        }
        Enseignant enseignant = new Enseignant();

        enseignant.setId(UUID.randomUUID());
        enseignant.setNom(dto.getNom());
        enseignant.setPrenom(dto.getPrenom());
        enseignant.setEmail(dto.getEmail());
        enseignant.setPassword(passwordEncoder.encode(dto.getPassword()));
        enseignant.setPhone(dto.getPhone());
        enseignant.setDateNaissance(dto.getDateNaissance());
        enseignant.setRole(Role.ENSEIGNANT);
        enseignant.setMatricule(generateUniqueMatricule());
        enseignant.setDateEmbauche(dto.getDateEmbauche());
        enseignant.setSpecialite(dto.getSpecialite());

        Enseignant save = userRepository.save(enseignant) ;
        return  userMapper.toEnseigmentResponse(save) ;

    }


    //Eleve Managment
    public EleveResponseDTO createEleve(CreateUserRequest dto)
    {
        if(userRepository.findByEmail(dto.getEmail()).isPresent())
        {
            throw new UserAlreadyExistsException("User already exists with email: " + dto.getEmail());
        }

        Eleve eleve = new Eleve();

        eleve.setId(UUID.randomUUID());
        eleve.setNom(dto.getNom());
        eleve.setPrenom(dto.getPrenom());
        eleve.setEmail(dto.getEmail());
        eleve.setPassword(passwordEncoder.encode(dto.getPassword()));
        eleve.setPhone(dto.getPhone());
        eleve.setDateNaissance(dto.getDateNaissance());
        eleve.setRole(Role.ELEVE);
        //eleve.setClasse();

        Eleve save = userRepository.save(eleve) ;

        return userMapper.toEleveResponse(save) ;


    }






}
