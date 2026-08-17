package com.example.MarketPlace.service;

import com.example.MarketPlace.dto.ReviewRequestDto;
import com.example.MarketPlace.dto.ReviewResponseDto;
import com.example.MarketPlace.entity.Booking;
import com.example.MarketPlace.entity.BookingStatus;
import com.example.MarketPlace.entity.Review;
import com.example.MarketPlace.entity.User;
import com.example.MarketPlace.repository.BookingRepository;
import com.example.MarketPlace.repository.ItemRepository;
import com.example.MarketPlace.repository.ReviewRepository;
import com.example.MarketPlace.repository.UserRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ItemRepository itemRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public ReviewResponseDto addReview(ReviewRequestDto dto, Long authorId) {
        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new NotFoundException("Замовлення не знайдено"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Користувача не знайдено"));

        // Перевіряємо, чи є автор відгуку саме орендарем цього бронювання
        if (!booking.getRenter().getId().equals(authorId)) {
            throw new AccessDeniedException("Ви не можете залишити відгук до цього замовлення");
        }

        // Перевіряємо, чи бронювання має статус COMPLETED
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new IllegalArgumentException("Заставити відгук можна тільки після завершення замовлення");
        }

        Review review = new Review();
        review.setBooking(booking);
        review.setAuthor(author);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        Review savedReview = reviewRepository.save(review);

        // Далі можеш додати свій return з потрібним DTO
        return new ReviewResponseDto(
                savedReview.getId(),
                savedReview.getBooking().getId(),
                savedReview.getAuthor().getFirst_name(),
                savedReview.getRating(),
                savedReview.getComment(),
                savedReview.getCreatedAt()
        );
    }


    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getByItem(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException("Товар з ID " + itemId + " не знайдено");
        }

        // Викликаємо reviewRepository і беремо дані з конкретного review із потоку
        return reviewRepository.findAllByBookingItemId(itemId)
                .stream()
                .map(review -> new ReviewResponseDto(
                        review.getId(),
                        review.getBooking().getId(),
                        review.getAuthor().getFirst_name(), // Використовуй гетер із валого класу User
                        review.getRating(),
                        review.getComment(),
                        review.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}