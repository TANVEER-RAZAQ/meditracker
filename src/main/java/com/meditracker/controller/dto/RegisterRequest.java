package com.meditracker.controller.dto;


import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class RegisterRequest {
    @NotBlank
    private String rfidUid;
    @NotBlank
    private String fullName;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    public String getRfidUid() { return rfidUid; }
    public void setRfidUid(String rfidUid) { this.rfidUid = rfidUid; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
}