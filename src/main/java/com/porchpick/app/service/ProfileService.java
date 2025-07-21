package com.porchpick.app.service;

import com.porchpick.app.dto.ProfileRequestDto;
import com.porchpick.app.dto.ProfileResponseDto;
import com.porchpick.app.exception.ResourceNotFoundException;
import com.porchpick.app.model.Profile;
import com.porchpick.app.model.User;
import com.porchpick.app.repository.ProfileRepository;
import com.porchpick.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ProfileService implements IProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProfileResponseDto createProfile(UUID userId, ProfileRequestDto profileRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (user.getProfile() != null) {
            throw new IllegalStateException("User already has a profile.");
        }

        Profile profile = modelMapper.map(profileRequestDto, Profile.class);
        profile.setUser(user);

        Profile savedProfile = profileRepository.save(profile);
        return modelMapper.map(savedProfile, ProfileResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponseDto getProfileByUserId(UUID userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "userId", userId));
        return modelMapper.map(profile, ProfileResponseDto.class);
    }

    @Override
    public ProfileResponseDto updateProfile(UUID userId, ProfileRequestDto profileRequestDto) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "userId", userId));

        modelMapper.map(profileRequestDto, profile);

        Profile updatedProfile = profileRepository.save(profile);
        return modelMapper.map(updatedProfile, ProfileResponseDto.class);
    }
} 