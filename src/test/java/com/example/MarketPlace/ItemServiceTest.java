package com.example.MarketPlace;

import com.example.MarketPlace.entity.Item;
import com.example.MarketPlace.repository.ItemRepository;
import com.example.MarketPlace.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void findByOwnerId_ShouldReturnListOfItems() {
        Item item = new Item();
        item.setId(10L);
        item.setTitle("Laptop");

        when(itemRepository.findByOwnerId(1L)).thenReturn(List.of(item));

        List<Item> items = itemRepository.findByOwnerId(1L);

        assertFalse(items.isEmpty());
        assertEquals(1, items.size());
        assertEquals("Laptop", items.get(0).getTitle());
    }
}