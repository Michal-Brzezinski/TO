package com.nbp.app.model;

import jakarta.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeRate {
    private String currencyName;
    private int multiplier;
    private String currencyCode;
    private String averageRate;

    @XmlElement(name = "nazwa_waluty")
    public String getCurrencyName() { return currencyName; }
    public void setCurrencyName(String currencyName) { this.currencyName = currencyName; }

    @XmlElement(name = "przelicznik")
    public int getMultiplier() { return multiplier; }
    public void setMultiplier(int multiplier) { this.multiplier = multiplier; }

    @XmlElement(name = "kod_waluty")
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    @XmlElement(name = "kurs_sredni")
    public String getAverageRate() { return averageRate; }
    public void setAverageRate(String averageRate) { this.averageRate = averageRate; }

    public BigDecimal getRateValue() {
        return new BigDecimal(averageRate.replace(",", "."));
    }

    public BigDecimal getAdjustedRate() {
        return getRateValue().divide(new BigDecimal(this.multiplier), 4, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return String.format("%-35s | %-5s | %-10d | %-15s", currencyName, currencyCode, multiplier, averageRate);
    }
}