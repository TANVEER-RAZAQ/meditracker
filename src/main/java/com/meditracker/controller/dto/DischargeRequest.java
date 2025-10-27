package com.meditracker.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class DischargeRequest {
    
    @NotBlank(message = "RFID UID is required")
    private String rfidUid;
    
    public DischargeRequest() {}
    
    public DischargeRequest(String rfidUid) {
        this.rfidUid = rfidUid;
    }

    public String getRfidUid() {
        return rfidUid;
    }

    public void setRfidUid(String rfidUid) {
        this.rfidUid = rfidUid;
    }
}

