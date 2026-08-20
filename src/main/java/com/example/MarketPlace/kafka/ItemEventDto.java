package com.example.MarketPlace.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemEventDto implements Serializable {

    private Long itemId;
    private String title;
    private String action; // Наприклад: "CREATED", "UPDATED", "DELETED"
    private String message;
}