package com.meditracker.controller.dto;

import com.meditracker.domain.enums.Department;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class DoctorRequest {
    
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @NotNull(message = "Department is required")
    private Department department;
    
    private String roomNumber;
    private String floor;
    
    @DecimalMin(value = "0.0", message = "Consultation fee must be positive")
    private BigDecimal consultationFee;

    public DoctorRequest() {}

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }
}

