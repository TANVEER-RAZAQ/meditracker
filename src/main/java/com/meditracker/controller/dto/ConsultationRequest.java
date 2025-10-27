package com.meditracker.controller.dto;


import jakarta.validation.constraints.NotBlank;

public class ConsultationRequest {
    @NotBlank
    private String diagnosis;
    @NotBlank
    private String medications;
    private boolean testsNeeded;

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getMedications() { return medications; }
    public void setMedications(String medications) { this.medications = medications; }
    public boolean isTestsNeeded() { return testsNeeded; }
    public void setTestsNeeded(boolean testsNeeded) { this.testsNeeded = testsNeeded; }
}