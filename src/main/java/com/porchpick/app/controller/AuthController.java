package com.porchpick.app.controller;

import com.porchpick.app.dto.AuthRequestDto;
import com.porchpick.app.dto.AuthResponseDto;
import com.porchpick.app.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@Valid @RequestBody AuthRequestDto authRequestDto) {
        AuthResponseDto authResponseDto = authService.signup(authRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequestDto) {
        AuthResponseDto authResponseDto = authService.login(authRequestDto);
        return ResponseEntity.ok(authResponseDto);
    }
}
