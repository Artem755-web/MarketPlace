package com.example.MarketPlace.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {

    private Long id;

    private Long bookingId;
    private String first_name;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
