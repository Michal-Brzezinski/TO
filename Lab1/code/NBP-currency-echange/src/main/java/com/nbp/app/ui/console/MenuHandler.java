package com.nbp.app.ui.console;

import com.nbp.app.core.ExchangeApp;

public class MenuHandler extends ConsoleHandler {

    public MenuHandler(String parameter, ExchangeApp app) {
        super(parameter, app);
    }

    @Override
    protected void handleConcrete() {
        System.out.println("\n--- MENU ---");
        System.out.println("1: Display currency exchange rates table");
        System.out.println("2: Exchange currency");
        System.out.println("0: Exit");
        System.out.print("Select an option: ");
    }

    @Override
    protected boolean checkCondition(String command) {
        // This is the default handler, triggered by an empty command
        return command.isEmpty();
    }
}