package com.example.MarketPlace.repository;

import com.example.MarketPlace.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long ownerId);

    List<Item> findByAvailableTrue();

    List<Item> findByCategoryId(Long categoryId);

    List<Item> findByTitleContainingIgnoreCase(String title);

    List<Item> findAllByOwnerId(Long ownerId); // Виправлено: додано параметр Long ownerId

//    List<Item> findAllByBookingItemId(Long itemId);
}