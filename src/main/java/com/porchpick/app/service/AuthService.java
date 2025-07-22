package com.porchpick.app.service;

import com.porchpick.app.dto.AuthRequestDto;
import com.porchpick.app.dto.AuthResponseDto;
import com.porchpick.app.exception.ResourceNotFoundException;
import com.porchpick.app.model.Profile;
import com.porchpick.app.model.User;
import com.porchpick.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ModelMapper modelMapper;

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
        
        userRepository.save(user);

        return AuthResponseDto.builder()
                .success(true)
                .message("User registered successfully. Please login.")
                .build();
    }

    @Override
    public AuthResponseDto login(AuthRequestDto authRequestDto) {
        User user = userRepository.findByEmail(authRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));

        if (!passwordEncoder.matches(authRequestDto.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password.");
        }

        String token = jwtService.generateToken(user.getId());

        return AuthResponseDto.builder()
                .success(true)
                .message("Login successful.")
                .id(user.getId())
                .email(user.getEmail())
                .token(token)
                .build();
    }
} 