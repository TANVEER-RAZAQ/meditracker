package com.meditracker.controller.dto;

import com.meditracker.domain.enums.LabTestStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateLabStatusRequest {
    @NotNull
    private LabTestStatus status;
    private String resultText;

    public LabTestStatus getStatus() { return status; }
    public void setStatus(LabTestStatus status) { this.status = status; }
    public String getResultText() { return resultText; }
    public void setResultText(String resultText) { this.resultText = resultText; }
}