package com.example.MarketPlace.repository;

import com.example.MarketPlace.entity.Booking;
import com.example.MarketPlace.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRenterId(Long renterId);

    // Spring Data підтримує вкладені поля: Item -> Owner -> Id
    List<Booking> findByItemOwnerId(Long ownerId);

    List<Booking> findAllByRenterId(Long userId);
    List<Booking> findByItemIdAndStatus(Long itemId, BookingStatus status);
    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.status = 'APPROVED' " +
            "AND b.startDate < :newEndDate " +
            "AND b.endDate > :newStartDate")
    boolean existsOverlappingBooking(@Param("itemId") Long itemId,
                                     @Param("newStartDate") LocalDate newStartDate,
                                     @Param("newEndDate") LocalDate newEndDate);
}
