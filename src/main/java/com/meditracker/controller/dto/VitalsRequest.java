package com.meditracker.controller.dto;



import jakarta.validation.constraints.NotNull;

public class VitalsRequest {
    @NotNull
    private Double temperatureCelsius;
    @NotNull
    private Integer bpSystolic;
    @NotNull
    private Integer bpDiastolic;
    @NotNull
    private Integer heartRate;

    public Double getTemperatureCelsius() { return temperatureCelsius; }
    public void setTemperatureCelsius(Double temperatureCelsius) { this.temperatureCelsius = temperatureCelsius; }
    public Integer getBpSystolic() { return bpSystolic; }
    public void setBpSystolic(Integer bpSystolic) { this.bpSystolic = bpSystolic; }
    public Integer getBpDiastolic() { return bpDiastolic; }
    public void setBpDiastolic(Integer bpDiastolic) { this.bpDiastolic = bpDiastolic; }
    public Integer getHeartRate() { return heartRate; }
    public void setHeartRate(Integer heartRate) { this.heartRate = heartRate; }
}