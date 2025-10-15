package com.nbp.app.ui.console;

import com.nbp.app.core.ExchangeApp;
import com.nbp.app.service.ExchangeService;
import com.nbp.app.service.dto.ExchangeResult;
import java.math.BigDecimal;
import java.util.Scanner;

public class ExchangeCurrencyHandler extends ConsoleHandler {

    public ExchangeCurrencyHandler(String parameter, ExchangeApp app) {
        super(parameter, app);
    }

    @Override
    protected void handleConcrete() {
        if (!app.isDataReady()) {
            System.out.println("Data is still loading. Please wait a moment and try again.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter source currency code (e.g., EUR): ");
            String sourceCode = scanner.nextLine().toUpperCase();

            System.out.print("Enter amount to exchange: ");
            BigDecimal amount = new BigDecimal(scanner.nextLine().replace(",", "."));

            System.out.print("Enter target currency code (e.g., USD): ");
            String targetCode = scanner.nextLine().toUpperCase();

            // Delegacja logiki do serwisu
            ExchangeService service = app.getExchangeService();
            ExchangeResult result = service.performExchange(sourceCode, targetCode, amount);

            // Prezentacja wyniku
            System.out.println(result.message());

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount entered. Please use a valid number.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}