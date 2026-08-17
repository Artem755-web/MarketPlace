package com.example.MarketPlace.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {

    @NotBlank(message = "Email не може бути порожнім")
    @Email(message = "Некоректний формат електронної пошти")
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    private String first_name;

    private String last_name;



}
