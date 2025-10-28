package com.meditracker.controller;

import com.meditracker.controller.dto.LabTestDTO;
import com.meditracker.controller.dto.OrderLabTestRequest;
import com.meditracker.controller.dto.UpdateLabStatusRequest;
import com.meditracker.domain.LabTest;
import com.meditracker.domain.Visit;
import com.meditracker.domain.enums.LabTestStatus;
import com.meditracker.repository.LabTestRepository;
import com.meditracker.repository.VisitRepository;
import com.meditracker.service.LabService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lab")
public class LabController {

    private final LabService labService;
    private final LabTestRepository labTestRepository;
    private final VisitRepository visitRepository;
    
    public LabController(LabService labService, 
                        LabTestRepository labTestRepository,
                        VisitRepository visitRepository) {
        this.labService = labService;
        this.labTestRepository = labTestRepository;
        this.visitRepository = visitRepository;
    }

    @GetMapping
    public ResponseEntity<List<LabTestDTO>> getAllLabTests() {
        List<LabTestDTO> tests = labTestRepository.findAll().stream()
                .map(LabTestDTO::fromLabTest)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LabTestDTO>> getTestsByStatus(@PathVariable LabTestStatus status) {
        List<LabTestDTO> tests = labTestRepository.findByStatus(status).stream()
                .map(LabTestDTO::fromLabTest)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/visit/{visitId}")
    public ResponseEntity<List<LabTestDTO>> getTestsByVisit(@PathVariable Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));
        List<LabTestDTO> tests = labTestRepository.findByVisit(visit).stream()
                .map(LabTestDTO::fromLabTest)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/{labTestId}")
    public ResponseEntity<LabTest> getLabTest(@PathVariable Long labTestId) {
        LabTest test = labTestRepository.findById(labTestId)
                .orElseThrow(() -> new IllegalArgumentException("Lab test not found"));
        return ResponseEntity.ok(test);
    }

    @PostMapping("/{visitId}/order")
    public ResponseEntity<LabTest> order(@PathVariable Long visitId, @Valid @RequestBody OrderLabTestRequest request) {
        LabTest test = labService.orderTest(visitId, request.getTestName(), request.getPrice());
        return ResponseEntity.ok(test);
    }

    @PostMapping("/tests/{labTestId}/status")
    public ResponseEntity<LabTest> updateStatus(@PathVariable Long labTestId, @Valid @RequestBody UpdateLabStatusRequest request) {
        LabTest test = labService.updateStatus(labTestId, request.getStatus(), request.getResultText());
        return ResponseEntity.ok(test);
    }
}