package com.meditracker.controller.dto;


import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {
    @NotBlank
    private String rfidUid;
    @NotBlank
    private String fullName;

    public String getRfidUid() { return rfidUid; }
    public void setRfidUid(String rfidUid) { this.rfidUid = rfidUid; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}