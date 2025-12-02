package simulation.state;

import simulation.model.Person;
import java.awt.Color;

/**
 * Stan: Odporny na zakażenie.
 */
public class ImmuneState implements PersonState {

    @Override
    public boolean canBeInfected() {
        return false;
    }

    @Override
    public double getInfectionProbability() {
        return 0.0; // Odporny nie zakaża
    }

    @Override
    public void update(Person person, double deltaTime) {
        // Odporny osobnik nie wymaga aktualizacji stanu
    }

    @Override
    public Color getColor() {
        return new Color(0, 100, 255); // Niebieski
    }

    @Override
    public String getName() {
        return "Immune";
    }
}