package com.meditracker.controller;

import com.meditracker.controller.dto.OrderLabTestRequest;
import com.meditracker.controller.dto.UpdateLabStatusRequest;
import com.meditracker.domain.LabTest;
import com.meditracker.service.LabService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lab")
public class LabController {

    private final LabService labService;
    public LabController(LabService labService) { this.labService = labService; }

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