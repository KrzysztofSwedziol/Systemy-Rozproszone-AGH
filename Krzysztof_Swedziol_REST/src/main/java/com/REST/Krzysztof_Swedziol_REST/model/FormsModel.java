package com.REST.Krzysztof_Swedziol_REST.model;

public class FormsModel {
    private String sourceCurrency;
    private String targetCurrency;
    private double amount;

    public FormsModel() {}

    public FormsModel(String sourceCurrency, String targetCurrency, double amount) {
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.amount = amount;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


}
