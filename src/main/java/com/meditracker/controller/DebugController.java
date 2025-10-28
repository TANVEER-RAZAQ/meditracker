package com.meditracker.controller;

import com.meditracker.domain.Patient;
import com.meditracker.repository.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final PatientRepository patientRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    public DebugController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/check-patient/{rfidUid}")
    @Transactional
    public ResponseEntity<Map<String, Object>> checkPatient(@PathVariable String rfidUid) {
        Map<String, Object> result = new HashMap<>();
        
        // Clear any cached data
        entityManager.clear();
        
        // Force fresh read from database
        Patient patient = patientRepository.findByRfidUid(rfidUid).orElse(null);
        
        result.put("found", patient != null);
        if (patient != null) {
            result.put("id", patient.getId());
            result.put("name", patient.getFullName());
            result.put("rfid", patient.getRfidUid());
        }
        result.put("totalPatients", patientRepository.count());
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/force-refresh")
    public ResponseEntity<Map<String, Object>> forceRefresh() {
        Map<String, Object> result = new HashMap<>();
        entityManager.clear();
        result.put("cleared", true);
        result.put("totalPatients", patientRepository.count());
        return ResponseEntity.ok(result);
    }
}

