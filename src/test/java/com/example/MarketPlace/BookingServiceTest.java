package com.example.MarketPlace;

import com.example.MarketPlace.repository.BookingRepository;
import com.example.MarketPlace.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void existsOverlappingBooking_WhenOverlap_ShouldReturnTrue() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(3);

        when(bookingRepository.existsOverlappingBooking(1L, start, end)).thenReturn(true);

        boolean isOverlapping = bookingRepository.existsOverlappingBooking(1L, start, end);

        assertTrue(isOverlapping);
        verify(bookingRepository, times(1)).existsOverlappingBooking(1L, start, end);
    }
}