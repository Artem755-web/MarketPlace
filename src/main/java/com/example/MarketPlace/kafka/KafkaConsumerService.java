package com.example.MarketPlace.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "item-topic", groupId = "marketplace-group")
    public void listen(String message) {
        System.out.println("Отримано повідомлення з Kafka: " + message);
    }
}