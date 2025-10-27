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
    
    public void sendDischargeNotification(Long patientId, String patientName, String diagnosis) {
        String title = "Discharge Summary - Visit Completed";
        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(patientName).append(",\n\n");
        body.append("Your visit has been successfully completed.\n");
        
        if (diagnosis != null && !diagnosis.trim().isEmpty()) {
            body.append("Diagnosis: ").append(diagnosis).append("\n");
        }
        
        body.append("\nPlease follow your doctor's advice and take prescribed medications regularly.\n");
        body.append("Your complete visit summary is available in your patient portal.\n\n");
        body.append("🌟 Get well soon! 🌟\n\n");
        body.append("Thank you for choosing our healthcare services.\n");
        body.append("We wish you a speedy recovery!");
        
        // Send via FCM/SMS/Email
        log.info("[DISCHARGE] Sending discharge notification to patient {}", patientId);
        log.info("[DISCHARGE] Title: {}", title);
        log.info("[DISCHARGE] Body:\n{}", body.toString());
        
        // In production, this would integrate with:
        // - Firebase Cloud Messaging for push notifications
        // - SMS gateway for text messages
        // - Email service for detailed summaries
    }
}