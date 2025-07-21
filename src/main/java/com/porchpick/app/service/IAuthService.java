package com.porchpick.app.service;

import com.porchpick.app.dto.AuthRequestDto;
import com.porchpick.app.dto.AuthResponseDto;

public interface IAuthService {
    AuthResponseDto signup(AuthRequestDto authRequestDto);
    AuthResponseDto login(AuthRequestDto authRequestDto);
} 