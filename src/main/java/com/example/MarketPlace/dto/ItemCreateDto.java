package com.example.MarketPlace.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateDto {

    @NotBlank
    private String title;
    private String description;
    @NotNull(message = "Ціна за день є обов'язковою")
    @Positive(message = "Ціна за день повинна бути більшою за 0")
    private BigDecimal pricePerDay;
    @NotNull
    private Long categoryId;

}
