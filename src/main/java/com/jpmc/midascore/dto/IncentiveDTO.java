package com.jpmc.midascore.dto;

public class IncentiveDTO {

    private double amount;

    public IncentiveDTO() {}

    public IncentiveDTO(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

