package com.example.MarketPlace.controller;


import com.example.MarketPlace.dto.ReviewRequestDto;
import com.example.MarketPlace.dto.ReviewResponseDto;
import com.example.MarketPlace.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    @PostMapping
    @Operation(
            summary = "Залишити відгук",
            description = "Створює новий відгук до товару від імені поточного авторизованого користувача"
    )
    public ResponseEntity<ReviewResponseDto> addReview(
            @Valid @RequestBody ReviewRequestDto dto,
            @AuthenticationPrincipal Long currentUserId
    ){
        ReviewResponseDto responseDto = reviewService.addReview(dto, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/item/{itemId}")
    @Operation(
            summary = "Отримати відгук для товару",
            description = "Повертає відгук за унікальним ідентифікатором товару (itemId)"
    )
    public ResponseEntity<ReviewResponseDto> getByItem(@PathVariable Long itemId){
        ReviewResponseDto reviews = (ReviewResponseDto) reviewService.getByItem(itemId);
        return ResponseEntity.ok(reviews);
    }
}
