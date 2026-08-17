package com.example.MarketPlace.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDto {
    @NotNull(message = "ID бронювання є обов'язковим")
    private Long bookingId;
    @NotNull(message = "Оцінка є обов'язковою")
    @Min(value = 1, message = "Мінімальна оцінка — 1")
    @Max(value = 5, message = "Максимальна оцінка — 5")
    private Integer rating;

    private String comment;
}
