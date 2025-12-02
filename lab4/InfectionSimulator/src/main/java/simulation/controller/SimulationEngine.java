package simulation.controller;

import simulation.model.SimulationArea;
import simulation.util.SimulationParameters;

import javax.swing.Timer;

/**
 * Silnik symulacji odpowiedzialny za timing i pętlę główną.
 */
public class SimulationEngine {

    private final SimulationArea area;
    private Timer timer;
    private boolean running;
    private Runnable onUpdateCallback;

    public SimulationEngine(SimulationArea area) {
        this.area = area;
        this.running = false;

        // Timer tykający co 40ms (25 FPS = 1000ms / 25)
        int delay = (int) (1000.0 / SimulationParameters.STEPS_PER_SECOND);

        this.timer = new Timer(delay, e -> {
            if (running) {
                step();
            }
        });
    }

    /**
     * Wykonuje jeden krok symulacji.
     */
    public void step() {
        area.update(SimulationParameters.DELTA_TIME);

        // Wywołaj callback (odświeżenie GUI)
        if (onUpdateCallback != null) {
            onUpdateCallback.run();
        }
    }

    /**
     * Uruchamia symulację.
     */
    public void start() {
        if (!running) {
            running = true;
            timer.start();
        }
    }

    /**
     * Pauzuje symulację.
     */
    public void pause() {
        running = false;
    }

    /**
     * Zatrzymuje symulację i resetuje timer.
     */
    public void stop() {
        running = false;
        timer.stop();
    }

    /**
     * Przełącza między pauzą a uruchomieniem.
     */
    public void toggle() {
        if (running) {
            pause();
        } else {
            start();
        }
    }

    /**
     * Ustawia callback wywoływany po każdej aktualizacji.
     */
    public void setOnUpdateCallback(Runnable callback) {
        this.onUpdateCallback = callback;
    }

    public boolean isRunning() {
        return running;
    }

    public SimulationArea getArea() {
        return area;
    }
}