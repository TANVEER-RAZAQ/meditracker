package com.meditracker.controller;

import com.meditracker.controller.dto.WalletTopUpRequest;
import com.meditracker.domain.Wallet;
import com.meditracker.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/topup")
    public ResponseEntity<Wallet> topUp(@Valid @RequestBody WalletTopUpRequest request) {
        Wallet wallet = walletService.topUp(
                request.getRfidUid(),
                request.getAmount(),
                request.getPaymentMethod()
        );
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/rfid/{rfidUid}")
    public ResponseEntity<Wallet> getByRfid(@PathVariable String rfidUid) {
        Wallet wallet = walletService.getWalletByRfid(rfidUid);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Wallet> getByPatientId(@PathVariable Long patientId) {
        Wallet wallet = walletService.getWalletByPatientId(patientId);
        return ResponseEntity.ok(wallet);
    }
}

