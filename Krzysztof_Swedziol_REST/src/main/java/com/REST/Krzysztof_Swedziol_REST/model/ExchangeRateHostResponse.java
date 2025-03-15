package com.REST.Krzysztof_Swedziol_REST.model;

public class ExchangeRateHostResponse {
    private String base;
    private String date;
    private boolean success;
    private java.util.Map<String, Double> rates;

    public String getBase() {
        return base;
    }
    public void setBase(String base) {
        this.base = base;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public java.util.Map<String, Double> getRates() {
        return rates;
    }
    public void setRates(java.util.Map<String, Double> rates) {
        this.rates = rates;
    }
}