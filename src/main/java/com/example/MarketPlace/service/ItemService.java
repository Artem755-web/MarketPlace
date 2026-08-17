package com.example.MarketPlace.service;

import com.example.MarketPlace.dto.CategoryDto;
import com.example.MarketPlace.dto.ItemCreateDto;
import com.example.MarketPlace.dto.ItemResponseDto;
import com.example.MarketPlace.entity.Category;
import com.example.MarketPlace.entity.Item;
import com.example.MarketPlace.entity.User;
import com.example.MarketPlace.repository.CategoryRepository;
import com.example.MarketPlace.repository.ItemRepository;
import com.example.MarketPlace.repository.UserRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;



@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;


    @Transactional
    public ItemResponseDto create(ItemCreateDto dto, Long ownerId){
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Не знайдено категорію"));
        User owner = userRepository.findById(ownerId).
                orElseThrow(() -> new RuntimeException("Не знайшли користувача!"));

        Item item = new Item();
        item.setTitle(dto.getTitle());
        item.setDescription(dto.getDescription());
        item.setPricePerDay(dto.getPricePerDay());
        item.setAvailable(true);
        item.setCategory(category);
        item.setOwner(owner);

        Item savedItem = itemRepository.save(item);
        CategoryDto categoryDto = new CategoryDto(category.getId(), category.getName());
        return new ItemResponseDto(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getPricePerDay(),
                item.isAvailable(),
                item.getOwner().getId(),
                categoryDto
        );

    }
    @Transactional(readOnly = true)
    public ItemResponseDto getItemById(Long id) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Предмет з не знайдено з id: " + id));

        return new ItemResponseDto(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getPricePerDay(),
                item.isAvailable(),
                item.getOwner().getId(),
                new CategoryDto(item.getCategory().getId(), item.getCategory().getName()) // Перетворюємо Category на CategoryDto
        );
    }
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAllAvailable(){
        Item item = new Item();
        return itemRepository.findByAvailableTrue().stream()
                .map(i -> new ItemResponseDto(
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getPricePerDay(),
                        item.isAvailable(),
                        item.getOwner().getId(),
                        new CategoryDto(item.getCategory().getId(), item.getCategory().getName())
                ))
                .collect(Collectors.toList());    }

    @Transactional(readOnly = true)
    public List<ItemResponseDto> getByOwner(Long ownerId) {
        // 1. Виправлено заперечення !
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("Користувача з ID " + ownerId + " не знайдено");
        }

        // 2. Додано мапінг сутності в DTO
        return itemRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(item -> new ItemResponseDto(
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getPricePerDay(),
                        item.isAvailable(),
                        item.getOwner().getId(),
                        new CategoryDto(item.getCategory().getId(), item.getCategory().getName())
                ))
                .collect(Collectors.toList());
    }
    @Transactional
    public ItemResponseDto update(Long itemId, ItemCreateDto dto, Long userId){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар з ID " + itemId + " не знайдено"));
        if(!item.getOwner().getId().equals(userId)){
            throw  new AccessDeniedException("Ви не можете редагувати чужий товар");
        }

        if (dto.getCategoryId() != null && !dto.getCategoryId().equals(item.getCategory().getId())) {
            Category newCategory = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Категорію з ID " + dto.getCategoryId() + " не знайдено"));
            item.setCategory(newCategory);
        }
        Item updatedItem = itemRepository.save(item);
        CategoryDto categoryDto = new CategoryDto(updatedItem.getCategory().getId(), updatedItem.getCategory().getName());

        return new ItemResponseDto(
                updatedItem.getId(),
                updatedItem.getTitle(),
                updatedItem.getDescription(),
                updatedItem.getPricePerDay(),
                updatedItem.isAvailable(),
                updatedItem.getOwner().getId(),
                categoryDto
        );

    }

}
