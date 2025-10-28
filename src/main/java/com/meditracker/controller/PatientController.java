package com.meditracker.controller;

import com.meditracker.controller.dto.PatientResponse;
import com.meditracker.domain.Patient;
import com.meditracker.domain.Visit;
import com.meditracker.repository.PatientRepository;
import com.meditracker.repository.VisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private static final Logger log = LoggerFactory.getLogger(PatientController.class);
    
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    public PatientController(PatientRepository patientRepository, VisitRepository visitRepository) {
        this.patientRepository = patientRepository;
        this.visitRepository = visitRepository;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        log.info("GET /api/patients - Getting all patients");
        List<PatientResponse> patients = patientRepository.findAll().stream()
                .map(PatientResponse::new)
                .collect(Collectors.toList());
        log.info("Found {} patients", patients.size());
        return ResponseEntity.ok(patients);
    }

    // More specific paths MUST come before generic path variables
    @GetMapping("/rfid/{rfidUid}")
    public ResponseEntity<Patient> getPatientByRfid(@PathVariable String rfidUid) {
        log.info("GET /api/patients/rfid/{} - Looking for patient by RFID", rfidUid);
        Patient patient = patientRepository.findByRfidUid(rfidUid)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with RFID: " + rfidUid));
        log.info("Found patient: {}", patient.getFullName());
        return ResponseEntity.ok(patient);  // Return Patient directly like registration does
    }

    // Generic /{id} comes LAST to avoid catching specific paths
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        log.info("GET /api/patients/{} - Looking for patient by ID", id);
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        log.info("Found patient: {}", patient.getFullName());
        return ResponseEntity.ok(patient);  // Return Patient directly like registration does
    }

    @GetMapping("/{patientId}/visits")
    public ResponseEntity<List<Visit>> getPatientVisits(@PathVariable Long patientId) {
        log.info("GET /api/patients/{}/visits - Getting visits for patient", patientId);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        List<Visit> visits = visitRepository.findByPatientOrderByCreatedAtDesc(patient);
        return ResponseEntity.ok(visits);
    }
}

