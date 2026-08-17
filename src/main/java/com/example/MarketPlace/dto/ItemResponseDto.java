package com.example.MarketPlace.dto;


import com.example.MarketPlace.entity.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDto {

    private Long id;
    private String title;
    private String description;
    private BigDecimal pricePerDay;
    private boolean isAvailable;
    private Long ownerId;
    private CategoryDto category;

}
