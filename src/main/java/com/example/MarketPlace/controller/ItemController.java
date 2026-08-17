package com.example.MarketPlace.controller;


import com.example.MarketPlace.dto.ItemCreateDto;
import com.example.MarketPlace.dto.ItemResponseDto;
import com.example.MarketPlace.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items/**")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    @PostMapping
    public ResponseEntity<ItemResponseDto> add(@RequestBody ItemCreateDto good,
                                               @AuthenticationPrincipal Long currentUserId){
        ItemResponseDto create = itemService.create(good, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> response(){
        List<ItemResponseDto> items = itemService.getAllAvailable();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> retGood(@PathVariable Long id){
        ItemResponseDto item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }
}
