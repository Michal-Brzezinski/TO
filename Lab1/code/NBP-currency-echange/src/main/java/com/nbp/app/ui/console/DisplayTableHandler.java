package com.nbp.app.ui.console;

import com.nbp.app.core.ExchangeApp;
import com.nbp.app.model.ExchangeTable;

public class DisplayTableHandler extends ConsoleHandler {
    public DisplayTableHandler(String parameter, ExchangeApp app) {
        super(parameter, app);
    }

    @Override
    protected void handleConcrete() {
        if (!app.isDataReady()) {
            System.out.println("Data is still loading. Please wait a moment and try again.");
            return;
        }

        ExchangeTable table = app.getExchangeTable();

        System.out.println("\n--- NBP Exchange Rates Table ---");
        System.out.println("Table Number: " + table.getTableNumber());
        System.out.println("Publication Date: " + table.getPublicationDate());
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("%-35s | %-5s | %-10s | %-15s\n", "Currency Name", "Code", "Multiplier", "Average Rate to PLN");
        System.out.println("-------------------------------------------------------------------------");
        table.getRates().forEach(System.out::println);
        System.out.println("-------------------------------------------------------------------------");
    }
}