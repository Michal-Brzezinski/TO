package simulation.controller;

import simulation.model.Person;
import simulation.util.SimulationParameters;

/**
 * Kontroler odpowiedzialny za ruch osobników.
 */
public class MovementController {

    private final SimulationParameters params;

    public MovementController(SimulationParameters params) {
        this.params = params;
    }

    /**
     * Aktualizuje ruch osobnika.
     * @param person Osobnik
     * @param deltaTime Czas od ostatniej aktualizacji
     * @return true jeśli osobnik powinien zostać usunięty (wyszedł poza obszar)
     */
    public boolean updateMovement(Person person, double deltaTime) {
        // Aktualizuj pozycję
        person.update(deltaTime);

        // Sprawdź kolizję z granicami
        boolean shouldRemove = person.handleBoundaryCollision(
                params.width,
                params.height,
                params.boundaryReturnProbability
        );

        // Losowo zmień kierunek (5% szansy co krok)
        if (Math.random() < 0.05) {
            person.randomlyChangeDirection(params.maxSpeed);
        }

        return shouldRemove;
    }

    /**
     * Oblicza odległość między dwoma osobnikami.
     */
    public static double getDistance(Person a, Person b) {
        // Oblicz różnicę wektorów pozycji
        vectors.IVector diff = a.getPosition().subtract(b.getPosition());

        // Zwróć moduł różnicy (odległość euklidesowa)
        return diff.abs();
    }
}