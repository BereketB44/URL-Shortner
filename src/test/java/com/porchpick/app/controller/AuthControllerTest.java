package com.porchpick.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.porchpick.app.dto.AuthRequestDto;
import com.porchpick.app.dto.AuthResponseDto;
import com.porchpick.app.exception.GlobalExceptionHandler;
import com.porchpick.app.service.IAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private IAuthService authService;

    @InjectMocks
    private AuthController authController;

    private AuthRequestDto authRequestDto;
    private AuthResponseDto loginResponseDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        authRequestDto = new AuthRequestDto("test@example.com", "password123");
        
        loginResponseDto = AuthResponseDto.builder()
                .success(true)
                .message("Login successful.")
                .id(UUID.randomUUID())
                .email("test@example.com")
                .token("some.jwt.token")
                .build();
    }

    @Test
    @DisplayName("POST /auth/signup - Success")
    void givenValidSignupRequest_whenSignup_thenReturnsCreated() throws Exception {
        AuthResponseDto signupResponse = AuthResponseDto.builder()
                .success(true)
                .message("User registered successfully. Please login.")
                .build();

        when(authService.signup(any(AuthRequestDto.class))).thenReturn(signupResponse);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully. Please login."));
    }

    @Test
    @DisplayName("POST /auth/signup - Invalid Email")
    void givenInvalidSignupRequest_whenSignup_thenReturnsBadRequest() throws Exception {
        AuthRequestDto invalidRequest = new AuthRequestDto("not-an-email", "password123");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    @DisplayName("POST /auth/login - Success")
    void givenValidLoginRequest_whenLogin_thenReturnsOk() throws Exception {
        when(authService.login(any(AuthRequestDto.class))).thenReturn(loginResponseDto);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful."))
                .andExpect(jsonPath("$.id").value(loginResponseDto.getId().toString()))
                .andExpect(jsonPath("$.email").value(loginResponseDto.getEmail()))
                .andExpect(jsonPath("$.token").value(loginResponseDto.getToken()));
    }

    @Test
    @DisplayName("POST /auth/login - Invalid Credentials")
    void givenInvalidLoginRequest_whenLogin_thenReturnsBadRequest() throws Exception {
        when(authService.login(any(AuthRequestDto.class))).thenThrow(new RuntimeException("Invalid email or password."));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid email or password."));
    }
}
