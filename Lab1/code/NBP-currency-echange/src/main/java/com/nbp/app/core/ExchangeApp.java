package com.nbp.app.core;

import com.nbp.app.loader.ExchangeTableLoader;
import com.nbp.app.model.ExchangeTable;
import com.nbp.app.service.ExchangeService;
import com.nbp.app.ui.console.*;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class ExchangeApp {
    private static volatile ExchangeApp instance;

    private volatile ExchangeTable exchangeTable = null;
    private volatile ExchangeService exchangeService = null;
    private final ConsoleHandler commandChain;

    private ExchangeApp() {
        this.commandChain = buildChain();

        ExchangeTableLoader loader = new ExchangeTableLoader();
        loader.loadDataAsync()
                .thenAccept(table -> {
                    this.exchangeTable = table;
                    this.exchangeService = new ExchangeService(table);
                    System.out.println("\nData ready. You can now use all application features.");
                })
                .exceptionally(error -> {
                    System.err.println("\n" + error.getCause().getMessage());
                    return null;
                });
    }

    public static ExchangeApp getInstance() {
        if (instance == null) {
            synchronized (ExchangeApp.class) {
                if (instance == null) {
                    instance = new ExchangeApp();
                }
            }
        }
        return instance;
    }

    private ConsoleHandler buildChain() {
        ConsoleHandler menu = new MenuHandler("", this);
        ConsoleHandler display = new DisplayTableHandler("1", this);
        ConsoleHandler exchange = new ExchangeCurrencyHandler("2", this);
        ConsoleHandler exit = new ExitHandler("0", this);
        ConsoleHandler invalid = new InvalidCommandHandler(null, this);

        menu.setNext(display);
        display.setNext(exchange);
        exchange.setNext(exit);
        exit.setNext(invalid);

        return menu;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        commandChain.handle(""); // Pokaż menu na starcie

        while (true) {
            String input = scanner.nextLine();
            commandChain.handle(input);
            if (!input.equals("0")) {
                System.out.print("\nSelect an option: ");
            }
        }
    }

    public boolean isDataReady() {
        return exchangeTable != null && exchangeService != null;
    }

    public ExchangeTable getExchangeTable() {
        if (!isDataReady()) {
            throw new IllegalStateException("Exchange data is not available yet.");
        }
        return exchangeTable;
    }

    public ExchangeService getExchangeService() {
        if (!isDataReady()) {
            throw new IllegalStateException("Exchange service is not available yet.");
        }
        return exchangeService;
    }
}