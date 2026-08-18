package com.example.MarketPlace.controller;


import com.example.MarketPlace.dto.ItemCreateDto;
import com.example.MarketPlace.dto.ItemResponseDto;
import com.example.MarketPlace.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items/**")
@Tag(name = "Item Controller", description = "Операції з предметами")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @Operation(
            summary = "Додати новий товар",
            description = "Створює новий товар або послугу та прив'язує його до поточного авторизованого користувача"
    )
    public ResponseEntity<ItemResponseDto> add(
            @RequestBody ItemCreateDto good,
            @AuthenticationPrincipal Long currentUserId) {

        ItemResponseDto create = itemService.create(good, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }

    @GetMapping
    @Operation(
            summary = "Отримати всі доступні товари",
            description = "Повертає список усіх товарів, які наразі доступні для бронювання"
    )
    public ResponseEntity<List<ItemResponseDto>> response() {
        List<ItemResponseDto> items = itemService.getAllAvailable();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Отримати предмет за ID",
            description = "Повертає деталі конкретного предмета за його унікальним ідентифікатором"
    )
    public ResponseEntity<ItemResponseDto> retGood(@PathVariable Long id) {
        ItemResponseDto item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }
}
