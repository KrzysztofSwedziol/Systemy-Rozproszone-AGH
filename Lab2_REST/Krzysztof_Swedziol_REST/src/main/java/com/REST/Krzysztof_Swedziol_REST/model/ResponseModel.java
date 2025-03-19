package com.REST.Krzysztof_Swedziol_REST.model;

public class ResponseModel {
    private double exchangeRate;
    private double calculationResult;

    public ResponseModel(){}

    public ResponseModel(double exchangeRate, double calculationResult) {
        this.exchangeRate = exchangeRate;
        this.calculationResult = calculationResult;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public double getCalculationResult() {
        return calculationResult;
    }

    public void setCalculationResult(double calculationResult) {
        this.calculationResult = calculationResult;
    }
}
