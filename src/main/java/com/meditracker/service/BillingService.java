package com.meditracker.service;

import com.meditracker.domain.*;
import com.meditracker.domain.enums.BillingStatus;
import com.meditracker.repository.BillingRepository;
import com.meditracker.repository.PatientRepository;
import com.meditracker.repository.VisitRepository;
import com.meditracker.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillingService {

    private final BillingRepository billingRepository;
    private final WalletRepository walletRepository;
    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final NotificationService notificationService;

    public BillingService(BillingRepository billingRepository,
                          WalletRepository walletRepository,
                          VisitRepository visitRepository,
                          PatientRepository patientRepository,
                          NotificationService notificationService) {
        this.billingRepository = billingRepository;
        this.walletRepository = walletRepository;
        this.visitRepository = visitRepository;
        this.patientRepository = patientRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public void payWithRfid(String rfidUid, Long billingId) {
        Patient patient = patientRepository.findByRfidUid(rfidUid)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        Wallet wallet = walletRepository.findByPatient(patient)
                .orElseThrow(() -> new IllegalStateException("Wallet not found"));
        Billing bill = billingRepository.findById(billingId)
                .orElseThrow(() -> new IllegalArgumentException("Billing item not found"));

        if (bill.getStatus() == BillingStatus.PAID) {
            return; // idempotent
        }

        BigDecimal balance = wallet.getBalance();
        if (balance.compareTo(bill.getAmount()) < 0) {
            throw new IllegalStateException("Insufficient wallet balance");
        }

        wallet.setBalance(balance.subtract(bill.getAmount()));
        bill.setStatus(BillingStatus.PAID);
        bill.setPaidAt(LocalDateTime.now());
        walletRepository.save(wallet);
        billingRepository.save(bill);

        notificationService.sendToPatient(patient.getId(), "Payment Success",
                "Paid " + bill.getAmount() + " for " + bill.getItemDescription());

        Visit visit = bill.getVisit();
        boolean anyUnpaid = billingRepository.findByVisit(visit).stream()
                .anyMatch(b -> b.getStatus() != BillingStatus.PAID);
        boolean anyPendingLab = false; // simplified
        if (!anyUnpaid && !anyPendingLab) {
            visit.setStatus(com.meditracker.domain.enums.VisitStatus.COMPLETED);
            visitRepository.save(visit);
            notificationService.sendToPatient(patient.getId(), "Visit Completed", "Thank you for visiting.");
        }
    }

    @Transactional(readOnly = true)
    public List<Billing> getByVisit(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));
        return billingRepository.findByVisit(visit);
    }
}