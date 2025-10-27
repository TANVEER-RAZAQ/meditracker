package com.meditracker.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendToPatient(Long patientId, String title, String body) {
        // Placeholder for Firebase Cloud Messaging integration
        log.info("[FCM] To patient {}: {} - {}", patientId, title, body);
    }
}