package com.nbp.app;

import com.nbp.app.core.ExchangeApp;

public class App {
    public static void main(String[] args) {
        ExchangeApp app = ExchangeApp.getInstance();
        app.run();
    }
}