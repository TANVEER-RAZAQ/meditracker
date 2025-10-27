package com.meditracker.service;

import com.meditracker.domain.Patient;
import com.meditracker.domain.Wallet;
import com.meditracker.repository.PatientRepository;
import com.meditracker.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class RegistrationService {

    private final PatientRepository patientRepository;
    private final WalletRepository walletRepository;
    private final NotificationService notificationService;

    public RegistrationService(PatientRepository patientRepository,
                               WalletRepository walletRepository,
                               NotificationService notificationService) {
        this.patientRepository = patientRepository;
        this.walletRepository = walletRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Patient registerOrFetch(String rfidUid, String fullName) {
        return patientRepository.findByRfidUid(rfidUid).orElseGet(() -> {
            Patient p = new Patient();
            p.setRfidUid(rfidUid);
            p.setFullName(fullName);
            Patient saved = patientRepository.save(p);

            Wallet wallet = new Wallet();
            wallet.setPatient(saved);
            wallet.setBalance(new BigDecimal("1000.00")); // seed funds for demo
            walletRepository.save(wallet);

            notificationService.sendToPatient(saved.getId(), "Registration Successful", "Welcome, " + fullName + "!");
            return saved;
        });
    }
}