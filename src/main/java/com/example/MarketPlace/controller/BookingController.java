package com.example.MarketPlace.controller;

import com.example.MarketPlace.dto.BookingRequestDto;
import com.example.MarketPlace.dto.BookingResponseDto;
import com.example.MarketPlace.entity.User;
import com.example.MarketPlace.service.BookingService;
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
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(
            @Valid @RequestBody BookingRequestDto dto,
            @AuthenticationPrincipal User userDetails) {

        Long userId = userDetails.getId();
        BookingResponseDto response = bookingService.create(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user")
    public ResponseEntity<List<BookingResponseDto>> getUserBookings(
            @AuthenticationPrincipal User userDetails) {

        Long userId = userDetails.getId();
        List<BookingResponseDto> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BookingResponseDto> updateBookingStatus(
            @PathVariable("id") Long bookingId,
            @RequestParam Boolean approved,
            @AuthenticationPrincipal User userDetails) {

        Long ownerId = userDetails.getId();
        BookingResponseDto response = bookingService.approveOrReject(bookingId, ownerId, approved);
        return ResponseEntity.ok(response);
    }
}