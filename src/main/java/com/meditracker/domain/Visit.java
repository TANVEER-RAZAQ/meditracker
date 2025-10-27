package com.meditracker.domain;
import com.meditracker.domain.base.Auditable;
import com.meditracker.domain.enums.Department;
import com.meditracker.domain.enums.VisitStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "visits")
public class Visit extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Patient patient;

    @ManyToOne(optional = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private VisitStatus status = VisitStatus.REGISTERED;

    private Double temperatureCelsius;
    private Integer bpSystolic;
    private Integer bpDiastolic;
    private Integer heartRate;

    @Column(length = 1024)
    private String diagnosis;

    @Column(length = 1024)
    private String medications;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public VisitStatus getStatus() { return status; }
    public void setStatus(VisitStatus status) { this.status = status; }
    public Double getTemperatureCelsius() { return temperatureCelsius; }
    public void setTemperatureCelsius(Double temperatureCelsius) { this.temperatureCelsius = temperatureCelsius; }
    public Integer getBpSystolic() { return bpSystolic; }
    public void setBpSystolic(Integer bpSystolic) { this.bpSystolic = bpSystolic; }
    public Integer getBpDiastolic() { return bpDiastolic; }
    public void setBpDiastolic(Integer bpDiastolic) { this.bpDiastolic = bpDiastolic; }
    public Integer getHeartRate() { return heartRate; }
    public void setHeartRate(Integer heartRate) { this.heartRate = heartRate; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getMedications() { return medications; }
    public void setMedications(String medications) { this.medications = medications; }
}