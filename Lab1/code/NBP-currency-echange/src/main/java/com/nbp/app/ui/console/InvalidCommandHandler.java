package com.nbp.app.ui.console;

import com.nbp.app.core.ExchangeApp;

public class InvalidCommandHandler extends ConsoleHandler {
    public InvalidCommandHandler(String parameter, ExchangeApp app) {
        super(parameter, app);
    }

    @Override
    protected void handleConcrete() {
        System.out.println("Invalid command. Please try again.");

    }

    @Override
    protected boolean checkCondition(String command) {
        // This is the last handler in the chain, it always handles the request if reached.
        return true;
    }
}