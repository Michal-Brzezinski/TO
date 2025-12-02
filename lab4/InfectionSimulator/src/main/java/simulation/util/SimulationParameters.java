package simulation.util;

import java.io.Serializable;

/**
 * Klasa przechowująca parametry symulacji.
 */
public class SimulationParameters implements Serializable {

    // Wymiary obszaru (metry)
    public int width = 50;
    public int height = 50;

    // Parametry populacji
    public int initialPopulation = 20;
    public double spawnRate = 0.5; // osobników na sekundę
    public int initialInfected = 2; // liczba zakażonych na starcie

    // Parametry zakażenia
    public double infectionDistance = 2.0; // metry
    public double infectionTime = 3.0; // sekundy
    public double infectionProbabilityOnEntry = 0.1; // 10%

    // Parametry odporności
    public double immunityProbability = 0.0; // 0% lub 0.3 (30%) dla scenariusza 2

    // Parametry ruchu
    public double maxSpeed = 2.5; // m/s
    public double boundaryReturnProbability = 0.5; // 50%

    // Parametry czasowe
    public static final int STEPS_PER_SECOND = 25;
    public static final double DELTA_TIME = 1.0 / STEPS_PER_SECOND;

    public SimulationParameters() {}

    public SimulationParameters(int width, int height) {
        this.width = width;
        this.height = height;
    }
}