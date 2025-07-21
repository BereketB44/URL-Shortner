package com.porchpick.app.controller;

import com.porchpick.app.dto.ProfileRequestDto;
import com.porchpick.app.dto.ProfileResponseDto;
import com.porchpick.app.service.IProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/profile")
public class ProfileController {

    @Autowired
    private IProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileResponseDto> createProfile(@PathVariable UUID userId, @Valid @RequestBody ProfileRequestDto profileRequestDto) {
        ProfileResponseDto createdProfile = profileService.createProfile(userId, profileRequestDto);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable UUID userId) {
        ProfileResponseDto profile = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<ProfileResponseDto> updateProfile(@PathVariable UUID userId, @Valid @RequestBody ProfileRequestDto profileRequestDto) {
        ProfileResponseDto updatedProfile = profileService.updateProfile(userId, profileRequestDto);
        return ResponseEntity.ok(updatedProfile);
    }
}
