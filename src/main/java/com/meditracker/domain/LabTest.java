package com.meditracker.domain;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.meditracker.domain.base.Auditable;
import com.meditracker.domain.enums.LabTestStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "lab_tests")
public class LabTest extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JsonBackReference("visit-labtests")
    private Visit visit;

    @Column(nullable = false, length = 120)
    private String testName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private LabTestStatus status = LabTestStatus.ORDERED;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(length = 2048)
    private String resultText;

    private LocalDateTime completedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Visit getVisit() { return visit; }
    public void setVisit(Visit visit) { this.visit = visit; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public LabTestStatus getStatus() { return status; }
    public void setStatus(LabTestStatus status) { this.status = status; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getResultText() { return resultText; }
    public void setResultText(String resultText) { this.resultText = resultText; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}