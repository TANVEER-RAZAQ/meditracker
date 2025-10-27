package com.meditracker.service;

import com.meditracker.domain.*;
import com.meditracker.domain.enums.LabTestStatus;
import com.meditracker.repository.BillingRepository;
import com.meditracker.repository.LabTestRepository;
import com.meditracker.repository.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class LabService {

    private final VisitRepository visitRepository;
    private final LabTestRepository labTestRepository;
    private final BillingRepository billingRepository;
    private final NotificationService notificationService;

    public LabService(VisitRepository visitRepository,
                      LabTestRepository labTestRepository,
                      BillingRepository billingRepository,
                      NotificationService notificationService) {
        this.visitRepository = visitRepository;
        this.labTestRepository = labTestRepository;
        this.billingRepository = billingRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public LabTest orderTest(Long visitId, String testName, BigDecimal price) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));
        LabTest test = new LabTest();
        test.setVisit(visit);
        test.setTestName(testName);
        test.setPrice(price);
        test.setStatus(LabTestStatus.ORDERED);
        LabTest saved = labTestRepository.save(test);

        Billing bill = new Billing();
        bill.setVisit(visit);
        bill.setType(com.meditracker.domain.enums.BillingType.LAB_TEST);
        bill.setItemDescription("Lab Test - " + testName);
        bill.setAmount(price);
        billingRepository.save(bill);

        return saved;
    }

    @Transactional
    public LabTest updateStatus(Long labTestId, LabTestStatus status, String resultText) {
        LabTest labTest = labTestRepository.findById(labTestId)
                .orElseThrow(() -> new IllegalArgumentException("Lab test not found"));
        labTest.setStatus(status);
        if (status == LabTestStatus.COMPLETED) {
            labTest.setCompletedAt(LocalDateTime.now());
            labTest.setResultText(resultText);
            notificationService.sendToPatient(labTest.getVisit().getPatient().getId(),
                    "Test Results Ready", labTest.getTestName() + " results are available.");
        }
        return labTestRepository.save(labTest);
    }
}