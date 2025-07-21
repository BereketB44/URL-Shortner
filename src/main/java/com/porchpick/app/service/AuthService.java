package com.porchpick.app.service;

import com.porchpick.app.dto.AuthRequestDto;
import com.porchpick.app.dto.AuthResponseDto;
import com.porchpick.app.model.User;
import com.porchpick.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDto signup(AuthRequestDto authRequestDto) {
        if (userRepository.findByEmail(authRequestDto.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists.");
        }

        String hashedPassword = passwordEncoder.encode(authRequestDto.getPassword());

        User user = User.builder()
                .email(authRequestDto.getEmail())
                .passwordHash(hashedPassword)
                .build();

        User savedUser = userRepository.save(user);

        return new AuthResponseDto(savedUser.getId(), savedUser.getEmail());
    }

    @Override
    public AuthResponseDto login(AuthRequestDto authRequestDto) {
        User user = userRepository.findByEmail(authRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));

        if (!passwordEncoder.matches(authRequestDto.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password.");
        }

        return new AuthResponseDto(user.getId(), user.getEmail());
    }
} 