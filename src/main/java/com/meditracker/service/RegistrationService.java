package com.meditracker.service;

import com.meditracker.domain.Patient;
import com.meditracker.domain.Wallet;
import com.meditracker.repository.PatientRepository;
import com.meditracker.repository.WalletRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class RegistrationService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);
    
    private final PatientRepository patientRepository;
    private final WalletRepository walletRepository;
    private final NotificationService notificationService;
    
    @PersistenceContext
    private EntityManager entityManager;

    public RegistrationService(PatientRepository patientRepository,
                               WalletRepository walletRepository,
                               NotificationService notificationService) {
        this.patientRepository = patientRepository;
        this.walletRepository = walletRepository;
        this.notificationService = notificationService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Patient registerOrFetch(String rfidUid, String fullName, String phoneNumber, java.time.LocalDate dateOfBirth) {
        log.info("=== REGISTRATION START: RFID={} ===", rfidUid);
        
        // Check if patient already exists
        Optional<Patient> existing = patientRepository.findByRfidUid(rfidUid);
        if (existing.isPresent()) {
            log.info("Patient already exists: ID={}", existing.get().getId());
            return existing.get();
        }
        
        // Create new patient
        Patient p = new Patient();
        p.setRfidUid(rfidUid);
        p.setFullName(fullName);
        p.setPhoneNumber(phoneNumber);
        p.setDateOfBirth(dateOfBirth);
        
        log.info("Saving patient to database...");
        // Save patient with immediate flush
        Patient saved = patientRepository.saveAndFlush(p);
        entityManager.flush();  // Double flush to be absolutely sure
        
        log.info("Patient saved: ID={}", saved.getId());
        
        // Create wallet
        Wallet wallet = new Wallet();
        wallet.setPatient(saved);
        wallet.setBalance(new BigDecimal("1000.00"));
        walletRepository.saveAndFlush(wallet);
        
        log.info("Wallet created for patient ID={}", saved.getId());
        
        // Send notification
        try {
            notificationService.sendToPatient(saved.getId(), "Registration Successful", "Welcome, " + fullName + "!");
        } catch (Exception e) {
            log.warn("Notification failed but continuing: {}", e.getMessage());
        }
        
        // Verify patient was saved
        Optional<Patient> verification = patientRepository.findByRfidUid(rfidUid);
        if (verification.isEmpty()) {
            log.error("CRITICAL: Patient not found immediately after save!");
        } else {
            log.info("Verification successful: Patient found with ID={}", verification.get().getId());
        }
        
        log.info("=== REGISTRATION COMPLETE: RFID={}, ID={} ===", rfidUid, saved.getId());
        return saved;
    }
}