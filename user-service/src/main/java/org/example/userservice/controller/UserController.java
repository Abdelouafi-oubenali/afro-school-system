package org.example.userservice.controller;

import jakarta.validation.Valid;
import org.example.userservice.dto.AdminResponseDTO;
import org.example.userservice.dto.CreateUserRequest;
import org.example.userservice.dto.EnseignantResponseDTO;
import org.example.userservice.dto.UserResponseDTO;
import org.example.userservice.entity.Admin;
import org.example.userservice.entity.User;
import org.example.userservice.enums.Role;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
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


    // Admin mangement
    @PostMapping("/admins")
    public AdminResponseDTO createAdmin(@Valid @RequestBody CreateUserRequest dto) {
        return userService.createAdmin(dto);
    }


    // Enseignent mangement
    @PostMapping("enseignent")
    public EnseignantResponseDTO createEnseignent(@Valid @RequestBody CreateUserRequest dto)
    {
        return userService.createEnseignent(dto) ;
    }







}