package com.meditracker.controller;

import com.meditracker.controller.dto.RfidPaymentRequest;
import com.meditracker.domain.Billing;
import com.meditracker.service.BillingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billingService;
    public BillingController(BillingService billingService) { this.billingService = billingService; }

    @PostMapping("/pay")
    public ResponseEntity<Void> pay(@Valid @RequestBody RfidPaymentRequest request) {
        billingService.payWithRfid(request.getRfidUid(), request.getBillingId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/visit/{visitId}")
    public ResponseEntity<List<Billing>> byVisit(@PathVariable Long visitId) {
        return ResponseEntity.ok(billingService.getByVisit(visitId));
    }
}