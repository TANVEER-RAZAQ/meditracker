package com.meditracker.controller;

import com.meditracker.controller.dto.RegisterRequest;
import com.meditracker.domain.Patient;
import com.meditracker.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final RegistrationService registrationService;
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<Patient> register(@Valid @RequestBody RegisterRequest request) {
        Patient patient = registrationService.registerOrFetch(request.getRfidUid(), request.getFullName());
        return ResponseEntity.ok(patient);
    }
}