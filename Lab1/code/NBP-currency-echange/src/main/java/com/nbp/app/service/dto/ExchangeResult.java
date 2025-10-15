package com.nbp.app.service.dto;

import java.math.BigDecimal;

public record ExchangeResult(boolean isSuccess, BigDecimal resultAmount, String message) {

    public static ExchangeResult success(BigDecimal amount, String message) {
        return new ExchangeResult(true, amount, message);
    }

    public static ExchangeResult failure(String message) {
        return new ExchangeResult(false, null, message);
    }
}