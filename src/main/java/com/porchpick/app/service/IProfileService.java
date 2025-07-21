package com.porchpick.app.service;

import com.porchpick.app.dto.ProfileRequestDto;
import com.porchpick.app.dto.ProfileResponseDto;

import java.util.UUID;

public interface IProfileService {
    ProfileResponseDto createProfile(UUID userId, ProfileRequestDto profileRequestDto);
    ProfileResponseDto getProfileByUserId(UUID userId);
    ProfileResponseDto updateProfile(UUID userId, ProfileRequestDto profileRequestDto);
} 