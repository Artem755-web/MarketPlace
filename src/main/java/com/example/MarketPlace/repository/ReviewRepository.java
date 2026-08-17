package com.example.MarketPlace.repository;

import com.example.MarketPlace.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBookingItemId(Long itemId);

    List<Review> findByAuthorId(Long authorId);

    boolean existsByBookingId(Long bookingId);

    Collection<Review> findAllByBookingItemId(Long itemId);
}
