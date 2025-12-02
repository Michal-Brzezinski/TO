package simulation.controller;

import simulation.model.Person;
import simulation.state.*;
import simulation.util.SimulationParameters;

import java.util.List;

/**
 * Kontroler odpowiedzialny za mechanizm zakażania.
 */
public class InfectionController {

    private final SimulationParameters params;

    public InfectionController(SimulationParameters params) {
        this.params = params;
    }

    /**
     * Sprawdza i przetwarza zakażenia dla wszystkich osobników.
     * @param people Lista wszystkich osobników
     * @param deltaTime Czas od ostatniej aktualizacji
     */
    public void processInfections(List<Person> people, double deltaTime) {
        // Dla każdej pary osobników
        for (int i = 0; i < people.size(); i++) {
            Person personA = people.get(i);

            // Jeśli osoba A nie może zostać zakażona, pomiń
            if (!personA.getCurrentState().canBeInfected()) {
                continue;
            }

            boolean inRange = false;

            for (int j = 0; j < people.size(); j++) {
                if (i == j) continue;

                Person personB = people.get(j);

                // Sprawdź czy osoba B jest zakażona
                double infectionProb = personB.getCurrentState().getInfectionProbability();
                if (infectionProb == 0.0) {
                    // Osoba B nie jest zakażona
                    personA.resetExposureTimer(personB);
                    continue;
                }

                // Oblicz odległość
                double distance = MovementController.getDistance(personA, personB);

                if (distance <= params.infectionDistance) {
                    // W zasięgu zakażenia
                    inRange = true;
                    personA.updateExposure(personB, deltaTime);

                    // Sprawdź czy ekspozycja przekroczyła próg
                    if (personA.hasExceededExposureThreshold(personB, params.infectionTime)) {
                        // Próba zakażenia
                        if (Math.random() < infectionProb) {
                            // Zakażenie udane!
                            // 50% szansy na bezobjawowy, 50% na objawowy
                            PersonState newState;
                            if (Math.random() < 0.5) {
                                newState = new InfectedAsymptomaticState();
                            } else {
                                newState = new InfectedSymptomaticState();
                            }
                            personA.setState(newState);
                            personA.clearExposureTimers();
                            break; // Już zakażony, nie sprawdzaj dalej
                        } else {
                            // Zakażenie nie powiodło się, zresetuj timer dla tej osoby
                            personA.resetExposureTimer(personB);
                        }
                    }
                } else {
                    // Poza zasięgiem - zresetuj timer
                    personA.resetExposureTimer(personB);
                }
            }

            // Jeśli nie było żadnej ekspozycji, wyczyść wszystkie timery
            if (!inRange) {
                personA.clearExposureTimers();
            }
        }
    }
}