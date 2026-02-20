package org.example.userservice.controller;

import jakarta.validation.Valid;
import org.example.userservice.dto.*;
import org.example.userservice.entity.Admin;
import org.example.userservice.entity.Eleve;
import org.example.userservice.entity.Enseignant;
import org.example.userservice.entity.User;
import org.example.userservice.enums.Role;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.events.EndDocument;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService ;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    public Map<String, Object> testUsers() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("count", 3);
        response.put("users", List.of("Alice", "Bob", "Charlie"));
        return response;
    }


    @PostMapping("/create-admin/test")
    public Map<String, Object> createAdminUser(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = request.get("email");
            String password = request.get("password");

            if (email == null || password == null) {
                email = "admin@school.com";
                password = "admin123";
            }

            if (userRepository.findByEmail(email).isPresent()) {
                response.put("status", "error");
                response.put("message", "User already exists with email: " + email);
                return response;
            }

            Admin admin = new Admin();
            admin.setId(UUID.randomUUID());
            admin.setNom(request.getOrDefault("nom", "Admin"));
            admin.setPrenom(request.getOrDefault("prenom", "System"));
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setPhone(request.getOrDefault("phone", "0600000000"));
            admin.setDateNaissance(LocalDate.parse(request.getOrDefault("dateNaissance", "1990-01-01")));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            admin.setMatricule(request.getOrDefault("matricule", "ADM001"));

            userRepository.save(admin);

            response.put("status", "success");
            response.put("message", "Admin user created successfully");
            response.put("email", email);
            response.put("password", password);
            response.put("id", admin.getId().toString());

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error creating user: " + e.getMessage());
        }

        return response;
    }

    @GetMapping("/list")
    public Map<String, Object> listUsers() {
        Map<String, Object> response = new HashMap<>();

        try {
            var users = userRepository.findAll();
            response.put("status", "success");
            response.put("count", users.size());
            response.put("users", users);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }


    // Admin management
    @PostMapping("/admins")
    public AdminResponseDTO createAdmin(@Valid @RequestBody CreateUserRequest dto) {
        return userService.createAdmin(dto);
    }

    @GetMapping("/admins")
    public List<AdminResponseDTO> getAdminAllAdmins()
    {
        return userService.getAllAdmins() ;
    }

    @GetMapping("/admins/{id}")
    public AdminResponseDTO getAdminById(@PathVariable UUID id) {
        return userService.getAdminById(id);
    }


    @DeleteMapping("admins/{id}")
    public void deleteAdmin(@PathVariable UUID id)
    {
        userService.deleteAdmin(id);
    }

    @PutMapping("/admins/{id}")
    public AdminResponseDTO updateAdmin(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest dto
    ) {
        System.out.println("controller + =================== " + dto);
        return userService.updateAdmin(id, dto);
    }




    // Enseignent management ==================================================================================
    @PostMapping("enseignent")
    public EnseignantResponseDTO createEnseignent(@Valid @RequestBody CreateUserRequest dto)
    {
        return userService.createEnseignent(dto) ;
    }

    @GetMapping("/enseignent")
    public Page<EnseignantResponseDTO> getAllEnseigment(Pageable pageable)
    {
        return userService.getAllEnseignants(pageable) ;
    }

    @GetMapping("/enseignent/{id}")
    public EnseignantResponseDTO getEnseigmentById(@PathVariable UUID id)
    {
        return userService.getEnseignantById(id) ;
    }

    @PutMapping("/enseignent/{id}")
    public ResponseEntity<EnseignantResponseDTO> updateEnseigmentById(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest dto) {

        EnseignantResponseDTO updated = userService.updateEnseignant(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/enseignent/{id}")
    public void deleteEnseigment(@PathVariable UUID id)
    {
        userService.deleteEnseignant(id);
    }



    //Eleve management =======================================================================================
    @PostMapping("eleve")
    public EleveResponseDTO createEleve(@Valid @RequestBody CreateUserRequest dto)
    {
        return userService.createEleve(dto) ;
    }

    @GetMapping("/eleve")
    public List<EleveResponseDTO> getAllEleve()
    {
        return userService.getAllEleves() ;
    }

    @GetMapping("/eleve/{id}")
    public EleveResponseDTO getEleveById(@PathVariable UUID id)
    {
        return userService.getEleveById(id) ;
    }

    @DeleteMapping("/eleve/{id}")
    public void deleteEleve(@PathVariable UUID id)
    {
         userService.deleteEleve(id);
    }

    @PutMapping("eleve/{id}")
    public EleveResponseDTO updateEleve(@PathVariable UUID id,
    @Valid @RequestBody CreateUserRequest dto){
        return userService.updateEleve(id,dto) ;
    }

    @GetMapping({"/{classId}/class", "/class/{classId}"})
    public List<EleveResponseDTO> getClassIdByEleveId(@PathVariable UUID classId) {
        return userService.getEleveByClasseid(classId);

    }

    @PostMapping("/eleve/{id}/assign-class/{classId}")
    public EleveResponseDTO assignClassToEleve(@PathVariable UUID id, @PathVariable UUID classId) {
        return userService.assignClassToEleve(id, classId);
    }



    //parent management ===============================================================

    @PostMapping("parent")
    public ParentResponseDTO createParent(@Valid @RequestBody CreateUserRequest dto)
    {
        return userService.createParent(dto) ;
    }

    @GetMapping("/parent")
    public List<ParentResponseDTO> getAllParents()
    {
        return  userService.getAllParents();
    }

    @GetMapping("/parent/{id}")
    public ParentResponseDTO getparentById(@PathVariable UUID id)
    {
        return userService.getParentById(id);
    }

    @DeleteMapping("/parent/{id}")
    public void deleteParent(@PathVariable UUID id)
    {
        userService.deleteParent(id);
    }

    @PutMapping("/parent/{id}")
    public ParentResponseDTO updateParent(@PathVariable UUID id , @Valid @RequestBody UpdateParentRequest dto)
    {
        return userService.updateParent(id , dto) ;
    }

    // Feign client endpoints for class-service
    @GetMapping("/students/{id}/classe")
    public UUID getClasseIdByStudent(@PathVariable UUID id) {
        return userService.getClasseIdByStudent(id);
    }

    @PutMapping("/students/{id}/classe/{classeId}")
    public void assignClasseToStudent(@PathVariable UUID id, @PathVariable UUID classeId) {
        userService.assignClasseToStudentInternal(id, classeId);
    }

    @PutMapping("/enseignants/{id}/classe/{classeId}")
    public void assignEnseignantToClasse(@PathVariable UUID id, @PathVariable UUID classeId) {
        userService.assignEnseignantToClasseInternal(id, classeId);
    }

    @GetMapping("/enseignants/class/{classeId}")
    public List<EnseignantResponseDTO> getEnseignantsByClasseId(@PathVariable UUID classeId) {
        return userService.getEnseignantsByClasseId(classeId);
    }

    @GetMapping("/enseignents/{id}")
    public EnseignantResponseDTO getEnseignantById(@PathVariable UUID id) {
        return userService.getEnseignantById(id);
    }

}