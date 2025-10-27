package com.meditracker.controller.dto;

import com.meditracker.domain.enums.Department;
import com.meditracker.domain.enums.VisitStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VisitSummaryDTO {
    
    private Long visitId;
    private LocalDateTime visitDate;
    private VisitStatus status;
    
    // Patient Info
    private String patientName;
    private String patientPhone;
    private String rfidUid;
    
    // Doctor Info
    private String doctorName;
    private Department department;
    private String roomNumber;
    
    // Vitals
    private VitalsInfo vitals;
    
    // Consultation
    private String diagnosis;
    private String medications;
    
    // Lab Tests
    private List<LabTestInfo> labTests;
    
    // Billing
    private BillingSummary billing;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dischargedAt;
    
    // Nested classes for structured data
    public static class VitalsInfo {
        private Double temperatureCelsius;
        private Integer bpSystolic;
        private Integer bpDiastolic;
        private Integer heartRate;
        
        public VitalsInfo() {}
        
        public VitalsInfo(Double temp, Integer sys, Integer dia, Integer hr) {
            this.temperatureCelsius = temp;
            this.bpSystolic = sys;
            this.bpDiastolic = dia;
            this.heartRate = hr;
        }

        public Double getTemperatureCelsius() { return temperatureCelsius; }
        public void setTemperatureCelsius(Double temperatureCelsius) { this.temperatureCelsius = temperatureCelsius; }
        public Integer getBpSystolic() { return bpSystolic; }
        public void setBpSystolic(Integer bpSystolic) { this.bpSystolic = bpSystolic; }
        public Integer getBpDiastolic() { return bpDiastolic; }
        public void setBpDiastolic(Integer bpDiastolic) { this.bpDiastolic = bpDiastolic; }
        public Integer getHeartRate() { return heartRate; }
        public void setHeartRate(Integer heartRate) { this.heartRate = heartRate; }
    }
    
    public static class LabTestInfo {
        private Long id;
        private String testName;
        private String status;
        private BigDecimal price;
        private String resultText;
        private LocalDateTime completedAt;
        
        public LabTestInfo() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTestName() { return testName; }
        public void setTestName(String testName) { this.testName = testName; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public String getResultText() { return resultText; }
        public void setResultText(String resultText) { this.resultText = resultText; }
        public LocalDateTime getCompletedAt() { return completedAt; }
        public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    }
    
    public static class BillingSummary {
        private List<BillingItem> items;
        private BigDecimal totalAmount;
        private BigDecimal totalPaid;
        private BigDecimal totalDue;
        private boolean fullyPaid;
        
        public BillingSummary() {}

        public List<BillingItem> getItems() { return items; }
        public void setItems(List<BillingItem> items) { this.items = items; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        public BigDecimal getTotalPaid() { return totalPaid; }
        public void setTotalPaid(BigDecimal totalPaid) { this.totalPaid = totalPaid; }
        public BigDecimal getTotalDue() { return totalDue; }
        public void setTotalDue(BigDecimal totalDue) { this.totalDue = totalDue; }
        public boolean isFullyPaid() { return fullyPaid; }
        public void setFullyPaid(boolean fullyPaid) { this.fullyPaid = fullyPaid; }
    }
    
    public static class BillingItem {
        private Long id;
        private String type;
        private String description;
        private BigDecimal amount;
        private String status;
        private LocalDateTime paidAt;
        
        public BillingItem() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public LocalDateTime getPaidAt() { return paidAt; }
        public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
    }
    
    // Main class getters and setters
    public Long getVisitId() { return visitId; }
    public void setVisitId(Long visitId) { this.visitId = visitId; }
    public LocalDateTime getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDateTime visitDate) { this.visitDate = visitDate; }
    public VisitStatus getStatus() { return status; }
    public void setStatus(VisitStatus status) { this.status = status; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public String getPatientPhone() { return patientPhone; }
    public void setPatientPhone(String patientPhone) { this.patientPhone = patientPhone; }
    public String getRfidUid() { return rfidUid; }
    public void setRfidUid(String rfidUid) { this.rfidUid = rfidUid; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public VitalsInfo getVitals() { return vitals; }
    public void setVitals(VitalsInfo vitals) { this.vitals = vitals; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getMedications() { return medications; }
    public void setMedications(String medications) { this.medications = medications; }
    public List<LabTestInfo> getLabTests() { return labTests; }
    public void setLabTests(List<LabTestInfo> labTests) { this.labTests = labTests; }
    public BillingSummary getBilling() { return billing; }
    public void setBilling(BillingSummary billing) { this.billing = billing; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getDischargedAt() { return dischargedAt; }
    public void setDischargedAt(LocalDateTime dischargedAt) { this.dischargedAt = dischargedAt; }
}

