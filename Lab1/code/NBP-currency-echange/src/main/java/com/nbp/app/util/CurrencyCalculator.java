package com.nbp.app.util;

import com.nbp.app.model.ExchangeRate;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class CurrencyCalculator {
    private CurrencyCalculator() {
        // Private constructor to prevent instantiation
    }

    public static BigDecimal exchange(BigDecimal amount, ExchangeRate sourceRate, ExchangeRate targetRate) {
        BigDecimal amountInPln = amount.multiply(sourceRate.getAdjustedRate());
        return amountInPln.divide(targetRate.getAdjustedRate(), 2, RoundingMode.HALF_UP);
    }
}