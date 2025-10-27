package com.meditracker.service;

import com.meditracker.domain.Patient;
import com.meditracker.domain.Wallet;
import com.meditracker.repository.PatientRepository;
import com.meditracker.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final PatientRepository patientRepository;
    private final NotificationService notificationService;

    public WalletService(WalletRepository walletRepository,
                        PatientRepository patientRepository,
                        NotificationService notificationService) {
        this.walletRepository = walletRepository;
        this.patientRepository = patientRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Wallet topUp(String rfidUid, BigDecimal amount, String paymentMethod) {
        Patient patient = patientRepository.findByRfidUid(rfidUid)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with RFID: " + rfidUid));
        
        Wallet wallet = walletRepository.findByPatient(patient)
                .orElseThrow(() -> new IllegalStateException("Wallet not found for patient"));

        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);
        Wallet updated = walletRepository.save(wallet);

        notificationService.sendToPatient(
                patient.getId(),
                "Wallet Recharged",
                String.format("₹%.2f added to your wallet via %s. New balance: ₹%.2f",
                        amount, paymentMethod != null ? paymentMethod : "payment", newBalance)
        );

        return updated;
    }

    @Transactional(readOnly = true)
    public Wallet getWalletByRfid(String rfidUid) {
        Patient patient = patientRepository.findByRfidUid(rfidUid)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with RFID: " + rfidUid));
        
        return walletRepository.findByPatient(patient)
                .orElseThrow(() -> new IllegalStateException("Wallet not found for patient"));
    }

    @Transactional(readOnly = true)
    public Wallet getWalletByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        
        return walletRepository.findByPatient(patient)
                .orElseThrow(() -> new IllegalStateException("Wallet not found for patient"));
    }
}

