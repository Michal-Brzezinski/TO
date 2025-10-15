package com.nbp.app.ui.console;

import com.nbp.app.core.ExchangeApp;

public class ExitHandler extends ConsoleHandler {
    public ExitHandler(String parameter, ExchangeApp app) {
        super(parameter, app);
    }

    @Override
    protected void handleConcrete() {
        System.out.println("Exiting application...");
        System.exit(0);
    }
}