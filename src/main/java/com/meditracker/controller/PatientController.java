package com.meditracker.controller;

import com.meditracker.domain.Patient;
import com.meditracker.domain.Visit;
import com.meditracker.repository.PatientRepository;
import com.meditracker.repository.VisitRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    public PatientController(PatientRepository patientRepository, VisitRepository visitRepository) {
        this.patientRepository = patientRepository;
        this.visitRepository = visitRepository;
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/rfid/{rfidUid}")
    public ResponseEntity<Patient> getPatientByRfid(@PathVariable String rfidUid) {
        Patient patient = patientRepository.findByRfidUid(rfidUid)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with RFID: " + rfidUid));
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/{patientId}/visits")
    public ResponseEntity<List<Visit>> getPatientVisits(@PathVariable Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        List<Visit> visits = visitRepository.findByPatientOrderByCreatedAtDesc(patient);
        return ResponseEntity.ok(visits);
    }
}

