package com.meditracker.controller.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class OrderLabTestRequest {
    @NotBlank
    private String testName;
    @NotNull
    private BigDecimal price;

    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}