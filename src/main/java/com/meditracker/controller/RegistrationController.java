package com.meditracker.controller;

import com.meditracker.controller.dto.RegisterRequest;
import com.meditracker.domain.Patient;
import com.meditracker.repository.PatientRepository;
import com.meditracker.service.RegistrationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);
    
    private final RegistrationService registrationService;
    private final TransactionTemplate transactionTemplate;
    private final PatientRepository patientRepository;
    
    public RegistrationController(RegistrationService registrationService, 
                                  TransactionTemplate transactionTemplate,
                                  PatientRepository patientRepository) {
        this.registrationService = registrationService;
        this.transactionTemplate = transactionTemplate;
        this.patientRepository = patientRepository;
    }

    @PostMapping
    public ResponseEntity<Patient> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Controller received registration request for RFID: {}", request.getRfidUid());
        
        // Execute registration in explicit transaction
        Patient patient = transactionTemplate.execute(status -> {
            Patient p = registrationService.registerOrFetch(
                request.getRfidUid(), 
                request.getFullName(),
                request.getPhoneNumber(),
                request.getDateOfBirth()
            );
            log.info("Registration service returned patient ID: {}", p.getId());
            return p;
        });
        
        // Transaction is now committed - verify patient exists
        log.info("Transaction committed, verifying patient exists...");
        Patient verified = patientRepository.findByRfidUid(request.getRfidUid())
            .orElseThrow(() -> {
                log.error("CRITICAL: Patient not found after transaction commit!");
                return new IllegalStateException("Patient was not saved to database");
            });
        
        log.info("Verification successful! Patient ID: {} found in database", verified.getId());
        return ResponseEntity.ok(verified);
    }
}