package com.example.MarketPlace.service;

import com.example.MarketPlace.confing.JwtUtils;
import com.example.MarketPlace.dto.AuthRequestDto;
import com.example.MarketPlace.dto.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    public AuthResponseDto login(AuthRequestDto dto) {
        // 1. Перевіряємо email та пароль через Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        // 2. Завантажуємо деталі користувача
        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());

        // 3. Генеруємо токен
        String token = jwtUtils.generateToken(userDetails);

        return new AuthResponseDto(token);
    }
}