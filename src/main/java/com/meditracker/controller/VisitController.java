package com.meditracker.controller;

import com.meditracker.controller.dto.*;
import com.meditracker.domain.Visit;
import com.meditracker.domain.enums.Department;
import com.meditracker.domain.enums.VisitStatus;
import com.meditracker.repository.VisitRepository;
import com.meditracker.service.VisitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;
    private final VisitRepository visitRepository;
    
    public VisitController(VisitService visitService, VisitRepository visitRepository) {
        this.visitService = visitService;
        this.visitRepository = visitRepository;
    }

    @GetMapping
    public ResponseEntity<List<Visit>> getAllVisits() {
        List<Visit> visits = visitRepository.findAll();
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Visit>> getVisitsByStatus(@PathVariable VisitStatus status) {
        List<Visit> visits = visitRepository.findByStatus(status);
        return ResponseEntity.ok(visits);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<ActiveVisitDTO>> getActiveVisits() {
        // Get all visits that are not completed and convert to DTO
        List<ActiveVisitDTO> visits = visitRepository.findAll().stream()
                .filter(v -> v.getStatus() != VisitStatus.COMPLETED)
                .map(ActiveVisitDTO::fromVisit)
                .collect(Collectors.toList());
        return ResponseEntity.ok(visits);
    }
    
    @GetMapping("/department/{department}")
    public ResponseEntity<List<ActiveVisitDTO>> getVisitsByDepartment(@PathVariable Department department) {
        // Get all active visits for a specific department
        List<ActiveVisitDTO> visits = visitRepository.findByDepartmentAndStatusNot(department, VisitStatus.COMPLETED)
                .stream()
                .map(ActiveVisitDTO::fromVisit)
                .collect(Collectors.toList());
        return ResponseEntity.ok(visits);
    }

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

    @PostMapping("/discharge")
    public ResponseEntity<VisitSummaryDTO> discharge(@Valid @RequestBody DischargeRequest request) {
        VisitSummaryDTO summary = visitService.dischargePatient(request.getRfidUid());
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{visitId}/summary")
    public ResponseEntity<VisitSummaryDTO> getVisitSummary(@PathVariable Long visitId) {
        VisitSummaryDTO summary = visitService.getVisitSummary(visitId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/history/{rfidUid}")
    public ResponseEntity<List<VisitSummaryDTO>> getPatientHistory(@PathVariable String rfidUid) {
        List<VisitSummaryDTO> history = visitService.getPatientVisitHistory(rfidUid);
        return ResponseEntity.ok(history);
    }
}