package org.example.userservice.service;

import org.example.userservice.dto.UserDTO;
import org.example.userservice.entity.User;
import org.example.userservice.mapper.UserMapper;
import org.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO createUser(UserDTO dto) {
        User user = userMapper.toEntity(dto);

        // Générer UUID et encoder password
        user.setId(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }
}
