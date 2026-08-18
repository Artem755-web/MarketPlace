package com.example.MarketPlace.controller;

import com.example.MarketPlace.dto.BookingRequestDto;
import com.example.MarketPlace.dto.BookingResponseDto;
import com.example.MarketPlace.entity.User;
import com.example.MarketPlace.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@AllArgsConstructor
@Tag(name = "Booking Controller", description = "Управління бронюваннями та замовленнями")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Створити нове бронювання", description = "Реєструє нове бронювання для користувача")
    public ResponseEntity<BookingResponseDto> createBooking(
            @Valid @RequestBody BookingRequestDto dto,
            @AuthenticationPrincipal User userDetails) {

        Long userId = userDetails.getId();
        BookingResponseDto response = bookingService.create(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user")
    @Operation(summary = "Отримати деталі бронювання", description = "Повертає інформацію про бронювання за його ID")
    public ResponseEntity<List<BookingResponseDto>> getUserBookings(
            @AuthenticationPrincipal User userDetails) {

        Long userId = userDetails.getId();
        List<BookingResponseDto> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id}/status")
    @Operation(
            summary = "Підтвердити або відхилити бронювання",
            description = "Змінює статус бронювання (APPROVED / REJECTED). Виконувати дію може лише власник об'єкта."
    )
    public ResponseEntity<BookingResponseDto> updateBookingStatus(
            @PathVariable("id") Long bookingId,
            @RequestParam Boolean approved,
            @AuthenticationPrincipal User userDetails) {

        Long ownerId = userDetails.getId();
        BookingResponseDto response = bookingService.approveOrReject(bookingId, ownerId, approved);
        return ResponseEntity.ok(response);
    }
}