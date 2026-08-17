// AuthRequestDto.java
package com.example.MarketPlace.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}