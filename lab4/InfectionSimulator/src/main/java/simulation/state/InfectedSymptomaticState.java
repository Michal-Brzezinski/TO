package simulation.state;

import simulation.model.Person;
import java.awt.Color;

/**
 * Stan: Zakażony z objawami.
 */
public class InfectedSymptomaticState implements PersonState {

    private double infectionDuration; // Czas zakażenia (sekundy)
    private final double recoveryTime; // Czas do wyzdrowienia (20-30s losowo)

    public InfectedSymptomaticState() {
        // Losujemy czas wyzdrowienia między 20 a 30 sekund
        this.recoveryTime = 20.0 + Math.random() * 10.0;
        this.infectionDuration = 0.0;
    }

    @Override
    public boolean canBeInfected() {
        return false; // Już zakażony
    }

    @Override
    public double getInfectionProbability() {
        return 1.0; // 100% szansy na zakażenie innych
    }

    @Override
    public void update(Person person, double deltaTime) {
        infectionDuration += deltaTime;

        // Jeśli minął czas wyzdrowienia, przejdź do stanu odpornego
        if (infectionDuration >= recoveryTime) {
            person.setState(new ImmuneState());
        }
    }

    @Override
    public Color getColor() {
        return new Color(255, 0, 0); // Czerwony
    }

    @Override
    public String getName() {
        return "Infected (Symptomatic)";
    }

    public double getInfectionDuration() {
        return infectionDuration;
    }

    public double getRecoveryTime() {
        return recoveryTime;
    }
}