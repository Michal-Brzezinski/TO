package simulation.state;

import simulation.model.Person;
import java.awt.Color;
import java.io.Serializable;

/**
 * Interfejs reprezentujący stan osobnika (wzorzec Stan).
 */
public interface PersonState extends Serializable {

    /**
     * Czy osobnik może zostać zakażony w tym stanie?
     */
    boolean canBeInfected();

    /**
     * Zwraca prawdopodobieństwo zakażenia od tego osobnika (0.0 - 1.0).
     * 0.0 = nie zakaża, 0.5 = 50%, 1.0 = 100%
     */
    double getInfectionProbability();

    /**
     * Aktualizuje stan osobnika (np. licznik czasu zakażenia).
     * @param person Osobnik
     * @param deltaTime Czas od ostatniej aktualizacji (sekundy)
     */
    void update(Person person, double deltaTime);

    /**
     * Zwraca kolor reprezentujący ten stan.
     */
    Color getColor();

    /**
     * Zwraca nazwę stanu (do debugowania).
     */
    String getName();
}