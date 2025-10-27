package com.meditracker.service;

import com.meditracker.domain.*;
import com.meditracker.domain.enums.*;
import com.meditracker.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class VisitService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final VisitRepository visitRepository;
    private final BillingRepository billingRepository;
    private final NotificationService notificationService;

    public VisitService(PatientRepository patientRepository,
                        DoctorRepository doctorRepository,
                        VisitRepository visitRepository,
                        BillingRepository billingRepository,
                        NotificationService notificationService) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.visitRepository = visitRepository;
        this.billingRepository = billingRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Visit startVisit(String rfidUid, Department department) {
        Patient patient = patientRepository.findByRfidUid(rfidUid)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        Doctor doctor = doctorRepository.findFirstByDepartment(department)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found for department"));

        Visit visit = new Visit();
        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setDepartment(department);
        visit.setStatus(VisitStatus.REGISTERED);
        Visit saved = visitRepository.save(visit);

        Billing billing = new Billing();
        billing.setVisit(saved);
        billing.setType(BillingType.CONSULTATION);
        billing.setItemDescription("Consultation - " + doctor.getFullName());
        billing.setAmount(doctor.getConsultationFee() != null ? doctor.getConsultationFee() : new BigDecimal("300.00"));
        billingRepository.save(billing);

        notificationService.sendToPatient(patient.getId(), "Visit Started", "Assigned to Dr. " + doctor.getFullName());
        return saved;
    }

    @Transactional
    public Visit recordVitals(Long visitId, Double tempC, Integer sys, Integer dia, Integer hr) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));
        visit.setTemperatureCelsius(tempC);
        visit.setBpSystolic(sys);
        visit.setBpDiastolic(dia);
        visit.setHeartRate(hr);
        visit.setStatus(VisitStatus.VITALS);
        return visitRepository.save(visit);
    }

    @Transactional
    public Visit addConsultation(Long visitId, String diagnosis, String medications, boolean testsNeeded) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));
        visit.setDiagnosis(diagnosis);
        visit.setMedications(medications);
        visit.setStatus(testsNeeded ? VisitStatus.LAB_PENDING : VisitStatus.BILLING_PENDING);
        return visitRepository.save(visit);
    }
}