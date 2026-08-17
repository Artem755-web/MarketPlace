package com.example.MarketPlace.controller;

import com.example.MarketPlace.confing.JwtUtils;
import com.example.MarketPlace.dto.AuthRequestDto;
import com.example.MarketPlace.dto.AuthResponseDto;
import com.example.MarketPlace.dto.UserRegisterDto;

import com.example.MarketPlace.entity.User;
import com.example.MarketPlace.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterDto dto) {
        // Якщо реєстрацію обробляє authService або userService:
        // authService.register(dto);
        return ResponseEntity.ok("Користувач успішно зареєстрований!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto dto) {
        AuthResponseDto response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

}
