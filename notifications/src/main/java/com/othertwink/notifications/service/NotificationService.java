package com.othertwink.notifications.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @KafkaListener(topics = "sent_orders", groupId = "notifications_group")
    public void sendNotification(String message) {
        System.out.println("Notification sent for order: " + message);
    }
}

