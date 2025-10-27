package com.meditracker.controller.dto;



import com.meditracker.domain.enums.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StartVisitRequest {
    @NotBlank
    private String rfidUid;
    @NotNull
    private Department department;

    public String getRfidUid() { return rfidUid; }
    public void setRfidUid(String rfidUid) { this.rfidUid = rfidUid; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}