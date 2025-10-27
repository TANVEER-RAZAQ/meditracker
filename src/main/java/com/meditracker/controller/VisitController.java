package com.meditracker.controller;

import com.meditracker.controller.dto.ConsultationRequest;
import com.meditracker.controller.dto.StartVisitRequest;
import com.meditracker.controller.dto.VitalsRequest;
import com.meditracker.domain.Visit;
import com.meditracker.service.VisitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;
    public VisitController(VisitService visitService) { this.visitService = visitService; }

    @PostMapping("/start")
    public ResponseEntity<Visit> start(@Valid @RequestBody StartVisitRequest request) {
        Visit visit = visitService.startVisit(request.getRfidUid(), request.getDepartment());
        return ResponseEntity.ok(visit);
    }

    @PostMapping("/{visitId}/vitals")
    public ResponseEntity<Visit> recordVitals(@PathVariable Long visitId, @Valid @RequestBody VitalsRequest request) {
        Visit visit = visitService.recordVitals(visitId, request.getTemperatureCelsius(),
                request.getBpSystolic(), request.getBpDiastolic(), request.getHeartRate());
        return ResponseEntity.ok(visit);
    }

    @PostMapping("/{visitId}/consultation")
    public ResponseEntity<Visit> consultation(@PathVariable Long visitId, @Valid @RequestBody ConsultationRequest request) {
        Visit visit = visitService.addConsultation(visitId, request.getDiagnosis(),
                request.getMedications(), request.isTestsNeeded());
        return ResponseEntity.ok(visit);
    }
}