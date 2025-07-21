package com.porchpick.app.service;

import com.porchpick.app.dto.AuthRequestDto;
import com.porchpick.app.dto.AuthResponseDto;
import com.porchpick.app.model.User;
import com.porchpick.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private AuthRequestDto authRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        authRequestDto = new AuthRequestDto("test@example.com", "password");
        user = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .passwordHash("hashedPassword")
                .build();
    }

    @Test
    @DisplayName("Test signup - Success")
    void givenAuthRequestDto_whenSignup_thenReturnAuthResponseDto() {
        // given
        given(userRepository.findByEmail(authRequestDto.getEmail())).willReturn(Optional.empty());
        given(passwordEncoder.encode(authRequestDto.getPassword())).willReturn("hashedPassword");
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        AuthResponseDto result = authService.signup(authRequestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Test signup - User already exists")
    void givenExistingEmail_whenSignup_thenThrowsRuntimeException() {
        // given
        given(userRepository.findByEmail(authRequestDto.getEmail())).willReturn(Optional.of(user));

        // when & then
        assertThrows(RuntimeException.class, () -> {
            authService.signup(authRequestDto);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test login - Success")
    void givenValidCredentials_whenLogin_thenReturnAuthResponseDto() {
        // given
        given(userRepository.findByEmail(authRequestDto.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(authRequestDto.getPassword(), user.getPasswordHash())).willReturn(true);

        // when
        AuthResponseDto result = authService.login(authRequestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Test login - Invalid email")
    void givenInvalidEmail_whenLogin_thenThrowsRuntimeException() {
        // given
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> {
            authService.login(authRequestDto);
        });

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Test login - Invalid password")
    void givenInvalidPassword_whenLogin_thenThrowsRuntimeException() {
        // given
        given(userRepository.findByEmail(authRequestDto.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(authRequestDto.getPassword(), user.getPasswordHash())).willReturn(false);

        // when & then
        assertThrows(RuntimeException.class, () -> {
            authService.login(authRequestDto);
        });
    }
}
