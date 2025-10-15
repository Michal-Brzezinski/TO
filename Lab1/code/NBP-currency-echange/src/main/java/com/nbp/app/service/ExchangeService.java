package com.nbp.app.service;

import com.nbp.app.model.ExchangeRate;
import com.nbp.app.model.ExchangeTable;
import com.nbp.app.service.dto.ExchangeResult;
import com.nbp.app.util.CurrencyCalculator;

import java.math.BigDecimal;
import java.util.Optional;

public class ExchangeService {
    private final ExchangeTable exchangeTable;

    public ExchangeService(ExchangeTable exchangeTable) {
        this.exchangeTable = exchangeTable;
    }

    public ExchangeResult performExchange(String sourceCode, String targetCode, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ExchangeResult.failure("Error: Amount must be positive.");
        }

        Optional<ExchangeRate> sourceRateOpt = exchangeTable.getRate(sourceCode);
        if (sourceRateOpt.isEmpty()) {
            return ExchangeResult.failure("Error: Invalid source currency code.");
        }

        Optional<ExchangeRate> targetRateOpt = exchangeTable.getRate(targetCode);
        if (targetRateOpt.isEmpty()) {
            return ExchangeResult.failure("Error: Invalid target currency code.");
        }

        BigDecimal result = CurrencyCalculator.exchange(amount, sourceRateOpt.get(), targetRateOpt.get());
        String message = String.format("Result: %.2f %s is %.2f %s", amount, sourceCode, result, targetCode);

        return ExchangeResult.success(result, message);
    }
}