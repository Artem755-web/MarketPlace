
package com.example.MarketPlace.service;


import com.example.MarketPlace.dto.BookingRequestDto;
import com.example.MarketPlace.dto.BookingResponseDto;
import com.example.MarketPlace.entity.Booking;
import com.example.MarketPlace.entity.BookingStatus;
import com.example.MarketPlace.entity.Item;
import com.example.MarketPlace.entity.User;
import com.example.MarketPlace.repository.BookingRepository;
import com.example.MarketPlace.repository.ItemRepository;
import com.example.MarketPlace.repository.UserRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PasswordEncoder passwordEncoder;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookingResponseDto create(BookingRequestDto dto, Long renterId){

        User renter = userRepository.findById(renterId)
                .orElseThrow(() -> new NotFoundException("Користувача з ID " + renterId + " не знайдено"));
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new NotFoundException("Товар не знайдено"));
        if(item.getOwner().getId().equals(renterId)){
            throw new IllegalArgumentException("Власник не може бронювати власний товар");
        }

        long days = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate());

        if(days <= 0){
            throw new IllegalArgumentException("Дата закінчення має бути пізнішою за дату початку");
        }

        BigDecimal totalPrice = item.getPricePerDay().multiply(BigDecimal.valueOf(days));

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setRenter(renter);
        booking.setStartDate(dto.getStartDate());
        booking.setEndDate(dto.getEndDate());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);

        return new BookingResponseDto(
                savedBooking.getId(),
                savedBooking.getItem().getId(),
                savedBooking.getRenter().getId(),
                savedBooking.getStartDate(),
                savedBooking.getEndDate(),
                savedBooking.getTotalPrice(),
                savedBooking.getStatus(),
                savedBooking.getCreatedAt()
        );
    }

    @Transactional
    public BookingResponseDto approveOrReject(Long bookingId, Long ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронювання з ID " + bookingId + " не знайдено"));

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new IllegalArgumentException("Тільки власник товару може змінювати статус бронювання");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        Booking updatedBooking = bookingRepository.save(booking);

        return new BookingResponseDto(
                updatedBooking.getId(),
                updatedBooking.getItem().getId(),
                updatedBooking.getRenter().getId(),
                updatedBooking.getStartDate(),
                updatedBooking.getEndDate(),
                updatedBooking.getTotalPrice(),
                updatedBooking.getStatus(),
                updatedBooking.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getUserBookings(Long userId) {
        // 1. Перевіряємо існування користувача
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Користувача з ID " + userId + " не знайдено");
        }

        // 2. Шукаємо всі бронювання орендаря та мапимо в DTO
        return bookingRepository.findAllByRenterId(userId)
                .stream()
                .map(booking -> new BookingResponseDto(
                        booking.getId(),
                        booking.getItem().getId(),
                        booking.getRenter().getId(),
                        booking.getStartDate(),
                        booking.getEndDate(),
                        booking.getTotalPrice(),
                        booking.getStatus(),
                        booking.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
