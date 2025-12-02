package simulation.state;

import simulation.model.Person;
import java.awt.Color;

/**
 * Stan: Zdrowy i wrażliwy na zakażenie.
 */
public class HealthyState implements PersonState {

    @Override
    public boolean canBeInfected() {
        return true;
    }

    @Override
    public double getInfectionProbability() {
        return 0.0; // Zdrowy nie zakaża
    }

    @Override
    public void update(Person person, double deltaTime) {
        // Zdrowy osobnik nie wymaga aktualizacji stanu
    }

    @Override
    public Color getColor() {
        return new Color(0, 200, 0); // Zielony
    }

    @Override
    public String getName() {
        return "Healthy";
    }
}