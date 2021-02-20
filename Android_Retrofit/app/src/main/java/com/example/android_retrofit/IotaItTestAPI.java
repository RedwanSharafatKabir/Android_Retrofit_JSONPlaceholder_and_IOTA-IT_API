package com.example.android_retrofit;

public class IotaItTestAPI {
    private String name;
    private int principalAmount;
    private Double interestRate;
    private String tenureInMonth;

    public IotaItTestAPI() {}

    public IotaItTestAPI(String name, int principalAmount, Double interestRate, String tenureInMonth) {
        this.name = name;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.tenureInMonth = tenureInMonth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(int principalAmount) {
        this.principalAmount = principalAmount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public String getTenureInMonth() {
        return tenureInMonth;
    }

    public void setTenureInMonth(String tenureInMonth) {
        this.tenureInMonth = tenureInMonth;
    }
}
