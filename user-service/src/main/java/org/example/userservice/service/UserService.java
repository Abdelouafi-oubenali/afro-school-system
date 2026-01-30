package org.example.userservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.userservice.dto.*;
import org.example.userservice.entity.*;
import org.example.userservice.enums.Role;
import org.example.userservice.exception.ForbiddenException;
import org.example.userservice.exception.InvalidEnseignantException;
import org.example.userservice.exception.ResourceNotFoundException;
import org.example.userservice.exception.UserAlreadyExistsException;
import org.example.userservice.mapper.UserMapper;
import org.example.userservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    private EleveRepository eleveRepository ;

    @Autowired
    private EnseignantRepository enseignantRepository ;


    @Autowired
    ParentRepository parentRepository ;

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

    public List<AdminResponseDTO> getAllAdmins() {
        List<Admin> admins = userRepository.findAllByRole(Role.ADMIN);
        return admins.stream()
                .map(userMapper::toAdminResponse)
                .collect(Collectors.toList());
    }

    public Page<AdminResponseDTO> getAllAdmins(Pageable pageable) {
        Page<Admin> adminsPage = userRepository.findAllByRole(Role.ADMIN, pageable);
        return adminsPage.map(userMapper::toAdminResponse);
    }

    public AdminResponseDTO updateAdmin(UUID id, UpdateUserRequest dto) {
        Admin admin = userRepository.findByIdAndRole(id, Role.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));

        if (dto.getEmail() != null && !dto.getEmail().equals(admin.getEmail())) {
            userRepository.findByEmail(dto.getEmail())
                    .ifPresent(existingUser -> {
                        throw new UserAlreadyExistsException("Email already exists: " + dto.getEmail());
                    });
        }


        if (dto.getNom() != null) admin.setNom(dto.getNom());
        if (dto.getPrenom() != null) admin.setPrenom(dto.getPrenom());
        if (dto.getEmail() != null) admin.setEmail(dto.getEmail());
        if (dto.getPhone() != null) admin.setPhone(dto.getPhone());
        if (dto.getDateNaissance() != null) admin.setDateNaissance(dto.getDateNaissance());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Admin updated = userRepository.save(admin);
        return userMapper.toAdminResponse(updated);
    }

    public AdminResponseDTO getAdminById(UUID id)
    {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));

        return userMapper.toAdminResponse(admin) ;

    }

    public void deleteAdmin(UUID id) {
        Admin admin = userRepository.findByIdAndRole(id, Role.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));


        userRepository.delete(admin);
    }

    public void softDeleteAdmin(UUID id) {
        Admin admin = userRepository.findByIdAndRole(id, Role.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));

        admin.setActive(false);
        userRepository.save(admin);
    }


//=================================================================================================================
    //ENSEIGNANT management
//=================================================================================================================
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

    public EnseignantResponseDTO getEnseignantById(UUID id) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new ForbiddenException (
                        "Enseignant not found with id: " + id
                ));
        return userMapper.toEnseignantResponse(enseignant);
    }

    public EnseignantResponseDTO getEnseignantByEmail(String email) {
        Enseignant enseignant = enseignantRepository.findByEmail(email)
                .orElseThrow(() -> new ForbiddenException(
                        "Enseignant not found with email: " + email
                ));
        return userMapper.toEnseignantResponse(enseignant);
    }

    public List<EnseignantResponseDTO> getEnseignantsBySpecialite(String specialite) {
        List<Enseignant> enseignants = enseignantRepository.findBySpecialite(specialite);
        return enseignants.stream()
                .map(userMapper::toEnseignantResponse)
                .collect(Collectors.toList());
    }

    public List<EnseignantResponseDTO> getAllEnseignants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Enseignant> enseignants = enseignantRepository.findAllByRole(Role.ENSEIGNANT, pageable);

        return enseignants.stream()
                .map(userMapper::toEnseignantResponse)
                .collect(Collectors.toList());
    }

    public Page<EnseignantResponseDTO> getAllEnseignants(Pageable pageable) {
        Page<Enseignant> enseignantsPage = enseignantRepository.findAllByRole(Role.ENSEIGNANT, pageable);
        return enseignantsPage.map(userMapper::toEnseignantResponse);
    }
    public List<EnseignantResponseDTO> searchEnseignantsByName(String keyword) {
        List<Enseignant> enseignants = enseignantRepository.findByNomContainingOrPrenomContainingAndRole(
                keyword, keyword, Role.ENSEIGNANT
        );
        return enseignants.stream()
                .map(userMapper::toEnseignantResponse)
                .collect(Collectors.toList());
    }

    public EnseignantResponseDTO updateEnseignant(UUID id, UpdateUserRequest dto) {
        Enseignant enseignant = enseignantRepository.findByIdAndRole(id, Role.ENSEIGNANT)
                .orElseThrow(() -> new ForbiddenException(
                        "Enseignant not found with id: " + id
                ));


        if (dto.getNom() != null) enseignant.setNom(dto.getNom());
        if (dto.getPrenom() != null) enseignant.setPrenom(dto.getPrenom());
        if (dto.getEmail() != null) enseignant.setEmail(dto.getEmail());
        if (dto.getPhone() != null) enseignant.setPhone(dto.getPhone());
        if (dto.getDateNaissance() != null) enseignant.setDateNaissance(dto.getDateNaissance());

        if (dto.getSpecialite() != null) enseignant.setSpecialite(dto.getSpecialite());
        if (dto.getDateEmbauche() != null) enseignant.setDateEmbauche(dto.getDateEmbauche());


        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            enseignant.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Enseignant updated = userRepository.save(enseignant);
        return userMapper.toEnseignantResponse(updated);
    }

    public void deleteEnseignant(UUID id) {
        Enseignant enseignant = enseignantRepository.findByIdAndRole(id, Role.ENSEIGNANT)
                .orElseThrow(() -> new ForbiddenException(
                        "Enseignant not found with id: " + id
                ));

        userRepository.delete(enseignant);
    }


//===============================================================================================================
    //Eleve management
// ===============================================================================================================
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

    public EleveResponseDTO getEleveById(UUID id) {
        Eleve eleve = eleveRepository.findByIdAndRole(id, Role.ELEVE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Élève not found with id: " + id
                ));
        return userMapper.toEleveResponse(eleve);
    }

    public EleveResponseDTO getEleveByEmail(String email) {
        Eleve eleve = eleveRepository.findByEmailAndRole(email, Role.ELEVE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Élève not found with email: " + email
                ));
        return userMapper.toEleveResponse(eleve);
    }


    public List<EleveResponseDTO> getAllEleves() {
        List<Eleve> eleves = eleveRepository.findAllByRole(Role.ELEVE);
        return eleves.stream()
                .map(userMapper::toEleveResponse)
                .collect(Collectors.toList());
    }

    public Page<EleveResponseDTO> getAllEleves(Pageable pageable) {
        Page<Eleve> elevesPage = eleveRepository.findAllByRole(Role.ELEVE, pageable);
        return elevesPage.map(userMapper::toEleveResponse);
    }

    public List<EleveResponseDTO> searchElevesByName(String keyword) {
        List<Eleve> eleves = eleveRepository.findByNomContainingOrPrenomContainingAndRole(
                keyword, keyword, Role.ELEVE
        );
        return eleves.stream()
                .map(userMapper::toEleveResponse)
                .collect(Collectors.toList());
    }


    public void deleteEleve(UUID id) {
        Eleve eleve = eleveRepository.findByIdAndRole(id, Role.ELEVE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Élève not found with id: " + id
                ));

        userRepository.delete(eleve);
    }

    public void softDeleteEleve(UUID id) {
        Eleve eleve = eleveRepository.findByIdAndRole(id, Role.ELEVE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Élève not found with id: " + id
                ));

        eleve.setActive(false);
        userRepository.save(eleve);
    }


    public EleveResponseDTO updateEleve(UUID id, CreateUserRequest dto) {

        Eleve eleve = eleveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Eleve not found"));

        eleve.setNom(dto.getNom());
        eleve.setPrenom(dto.getPrenom());
        eleve.setPhone(dto.getPhone());
        eleve.setDateNaissance(dto.getDateNaissance());

        if(dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            eleve.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Eleve updated = userRepository.save(eleve);

        return userMapper.toEleveResponse(updated);
    }



//======================================================================================================================
    //Pqrent management
// /=====================================================================================================================


    public ParentResponseDTO createParent(CreateUserRequest dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(" email est dija utulser");
        }

        if (dto.getChildIds() == null || dto.getChildIds().isEmpty()) {
            throw new IllegalArgumentException("  Select au moin un envant ");
        }

        List<Eleve> children = eleveRepository.findAllById(dto.getChildIds());
        if (children.size() != dto.getChildIds().size()) {
            throw new IllegalArgumentException("Eleve no trouve pas");
        }

        Parent parent = new Parent();
        parent.setId(UUID.randomUUID());
        parent.setNom(dto.getNom());
        parent.setPrenom(dto.getPrenom());
        parent.setEmail(dto.getEmail());
        parent.setPassword(passwordEncoder.encode(dto.getPassword()));
        parent.setPhone(dto.getPhone());
        parent.setDateNaissance(dto.getDateNaissance());
        parent.setRole(Role.PARENT);

        parent.setChildIds(dto.getChildIds());

        Parent savedParent = (Parent) userRepository.save(parent);

        ParentResponseDTO response = userMapper.toParentResponse(savedParent);
        response.setChildIds(savedParent.getChildIds());

        return response;
    }

    public void addChildToParent(UUID parentId, UUID childId) {
        Parent parent = (Parent) userRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("parent no trouve"));

        eleveRepository.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException(" Eleve no trouver"));

        if (!parent.getChildIds().contains(childId)) {
            parent.getChildIds().add(childId);
            userRepository.save(parent);
        }
    }

    public ParentResponseDTO getParentById(UUID id) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parent non trouvé avec l'ID: " + id));
        return userMapper.toParentResponse(parent);
    }

    public ParentResponseDTO getParentByEmail(String email) {
        Parent parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Parent non trouvé avec l'email: " + email));
        return userMapper.toParentResponse(parent);
    }

    public List<ParentResponseDTO> getAllParents() {
        List<Parent> parents = parentRepository.findAll();
        return parents.stream()
                .map(userMapper::toParentResponse)
                .collect(Collectors.toList());
    }

    public List<ParentResponseDTO> getParentsByChildId(UUID childId) {
        List<Parent> parents = parentRepository.findByChildIdsContaining(childId);
        return parents.stream()
                .map(userMapper::toParentResponse)
                .collect(Collectors.toList());
    }


    public void deleteParent(UUID id) {
        if (!parentRepository.existsById(id)) {
            throw new EntityNotFoundException("Parent non trouvé avec l'ID: " + id);
        }
        parentRepository.deleteById(id);
    }


    public ParentResponseDTO updateParent(UUID parentId, UpdateParentRequest dto) {

        Parent parent = (Parent) userRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent non trouvé"));

        if (!parent.getEmail().equals(dto.getEmail())) {
            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException("Email déjà utilisé");
            }
            parent.setEmail(dto.getEmail());
        }

        if (dto.getChildIds() == null || dto.getChildIds().isEmpty()) {
            throw new IllegalArgumentException("Sélectionnez au moins un enfant");
        }

        List<Eleve> children = eleveRepository.findAllById(dto.getChildIds());
        if (children.size() != dto.getChildIds().size()) {
            throw new IllegalArgumentException("Un ou plusieurs élèves non trouvés");
        }

        parent.setNom(dto.getNom());
        parent.setPrenom(dto.getPrenom());
        parent.setPhone(dto.getPhone());
        parent.setDateNaissance(dto.getDateNaissance());
        parent.setChildIds(dto.getChildIds());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            parent.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Parent updatedParent = (Parent) userRepository.save(parent);

        ParentResponseDTO response = userMapper.toParentResponse(updatedParent);
        response.setChildIds(updatedParent.getChildIds());

        return response;
    }


}
