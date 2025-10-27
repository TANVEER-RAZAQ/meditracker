package com.meditracker.service;

import com.meditracker.controller.dto.VisitSummaryDTO;
import com.meditracker.domain.*;
import com.meditracker.domain.enums.*;
import com.meditracker.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final VisitRepository visitRepository;
    private final BillingRepository billingRepository;
    private final LabTestRepository labTestRepository;
    private final NotificationService notificationService;

    public VisitService(PatientRepository patientRepository,
                        DoctorRepository doctorRepository,
                        VisitRepository visitRepository,
                        BillingRepository billingRepository,
                        LabTestRepository labTestRepository,
                        NotificationService notificationService) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.visitRepository = visitRepository;
        this.billingRepository = billingRepository;
        this.labTestRepository = labTestRepository;
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

    @Transactional
    public VisitSummaryDTO dischargePatient(String rfidUid) {
        Patient patient = patientRepository.findByRfidUid(rfidUid)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with RFID: " + rfidUid));
        
        // Find the most recent non-completed visit
        Visit visit = visitRepository.findFirstByPatientAndStatusNotOrderByCreatedAtDesc(patient, VisitStatus.COMPLETED)
                .orElseThrow(() -> new IllegalStateException("No active visit found for patient"));
        
        // Validate all billing is complete
        List<Billing> billings = billingRepository.findByVisit(visit);
        boolean hasUnpaidBills = billings.stream()
                .anyMatch(b -> b.getStatus() != BillingStatus.PAID);
        
        if (hasUnpaidBills) {
            throw new IllegalStateException("Cannot discharge patient - unpaid bills exist");
        }
        
        // Validate all lab tests are complete (if any)
        List<LabTest> labTests = labTestRepository.findByVisit(visit);
        boolean hasIncompleteLabs = labTests.stream()
                .anyMatch(lt -> lt.getStatus() != LabTestStatus.COMPLETED);
        
        if (hasIncompleteLabs) {
            throw new IllegalStateException("Cannot discharge patient - lab tests are not completed");
        }
        
        // Mark visit as completed
        visit.setStatus(VisitStatus.COMPLETED);
        visitRepository.save(visit);
        
        // Generate and return summary
        VisitSummaryDTO summary = generateVisitSummary(visit);
        
        // Send discharge notification with "Get well soon!" message
        notificationService.sendDischargeNotification(
                patient.getId(), 
                patient.getFullName(), 
                visit.getDiagnosis()
        );
        
        return summary;
    }

    @Transactional(readOnly = true)
    public VisitSummaryDTO getVisitSummary(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));
        return generateVisitSummary(visit);
    }

    @Transactional(readOnly = true)
    public List<VisitSummaryDTO> getPatientVisitHistory(String rfidUid) {
        Patient patient = patientRepository.findByRfidUid(rfidUid)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with RFID: " + rfidUid));
        
        List<Visit> visits = visitRepository.findByPatientOrderByCreatedAtDesc(patient);
        return visits.stream()
                .map(this::generateVisitSummary)
                .collect(Collectors.toList());
    }

    private VisitSummaryDTO generateVisitSummary(Visit visit) {
        VisitSummaryDTO summary = new VisitSummaryDTO();
        
        // Basic visit info
        summary.setVisitId(visit.getId());
        summary.setVisitDate(visit.getCreatedAt());
        summary.setStatus(visit.getStatus());
        summary.setCreatedAt(visit.getCreatedAt());
        summary.setUpdatedAt(visit.getUpdatedAt());
        if (visit.getStatus() == VisitStatus.COMPLETED) {
            summary.setDischargedAt(visit.getUpdatedAt());
        }
        
        // Patient info
        Patient patient = visit.getPatient();
        summary.setPatientName(patient.getFullName());
        summary.setPatientPhone(patient.getPhoneNumber());
        summary.setRfidUid(patient.getRfidUid());
        
        // Doctor info
        Doctor doctor = visit.getDoctor();
        summary.setDoctorName(doctor.getFullName());
        summary.setDepartment(visit.getDepartment());
        summary.setRoomNumber(doctor.getRoomNumber());
        
        // Vitals
        VisitSummaryDTO.VitalsInfo vitals = new VisitSummaryDTO.VitalsInfo(
                visit.getTemperatureCelsius(),
                visit.getBpSystolic(),
                visit.getBpDiastolic(),
                visit.getHeartRate()
        );
        summary.setVitals(vitals);
        
        // Consultation details
        summary.setDiagnosis(visit.getDiagnosis());
        summary.setMedications(visit.getMedications());
        
        // Lab tests
        List<LabTest> labTests = labTestRepository.findByVisit(visit);
        List<VisitSummaryDTO.LabTestInfo> labTestInfos = labTests.stream()
                .map(lt -> {
                    VisitSummaryDTO.LabTestInfo info = new VisitSummaryDTO.LabTestInfo();
                    info.setId(lt.getId());
                    info.setTestName(lt.getTestName());
                    info.setStatus(lt.getStatus().toString());
                    info.setPrice(lt.getPrice());
                    info.setResultText(lt.getResultText());
                    info.setCompletedAt(lt.getCompletedAt());
                    return info;
                })
                .collect(Collectors.toList());
        summary.setLabTests(labTestInfos);
        
        // Billing summary
        List<Billing> billings = billingRepository.findByVisit(visit);
        VisitSummaryDTO.BillingSummary billingSummary = new VisitSummaryDTO.BillingSummary();
        
        List<VisitSummaryDTO.BillingItem> billingItems = billings.stream()
                .map(b -> {
                    VisitSummaryDTO.BillingItem item = new VisitSummaryDTO.BillingItem();
                    item.setId(b.getId());
                    item.setType(b.getType().toString());
                    item.setDescription(b.getItemDescription());
                    item.setAmount(b.getAmount());
                    item.setStatus(b.getStatus().toString());
                    item.setPaidAt(b.getPaidAt());
                    return item;
                })
                .collect(Collectors.toList());
        
        billingSummary.setItems(billingItems);
        billingSummary.setTotalAmount(billings.stream()
                .map(Billing::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        billingSummary.setTotalPaid(billings.stream()
                .filter(b -> b.getStatus() == BillingStatus.PAID)
                .map(Billing::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        billingSummary.setTotalDue(billings.stream()
                .filter(b -> b.getStatus() == BillingStatus.PENDING)
                .map(Billing::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        billingSummary.setFullyPaid(billingSummary.getTotalDue().compareTo(BigDecimal.ZERO) == 0);
        
        summary.setBilling(billingSummary);
        
        return summary;
    }
}