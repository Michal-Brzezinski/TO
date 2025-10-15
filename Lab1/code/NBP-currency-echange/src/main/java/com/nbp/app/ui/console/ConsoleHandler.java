package com.nbp.app.ui.console;

import com.nbp.app.core.ExchangeApp;

public abstract class ConsoleHandler {
    protected ConsoleHandler next;
    protected String parameter;
    protected final ExchangeApp app;

    public ConsoleHandler(String parameter, ExchangeApp app) {
        this.parameter = parameter;
        this.app = app;
    }

    public void setNext(ConsoleHandler next) {
        this.next = next;
    }

    public void handle(String command) {
        if (checkCondition(command)) {
            handleConcrete();
        } else if (next != null) {
            next.handle(command);
        }
    }

    protected boolean checkCondition(String command) {
        return this.parameter.equalsIgnoreCase(command);
    }

    protected abstract void handleConcrete();
}