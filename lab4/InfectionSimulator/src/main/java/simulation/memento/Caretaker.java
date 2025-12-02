package simulation.memento;

import simulation.model.Person;
import simulation.model.SimulationArea;
import vectors.Vector2D;
import vectors.decorator.Vector3DDecorator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa zarządzająca zapisem i odczytem stanów symulacji (wzorzec Pamiątka).
 */
public class Caretaker {

    /**
     * Tworzy memento z obecnego stanu symulacji.
     */
    public static SimulationMemento createMemento(SimulationArea area) {
        return new SimulationMemento(
                area.getPeople(),
                area.getSimulationTime(),
                area.getParams(),
                area.getTotalSpawned(),
                area.getTotalRemoved()
        );
    }

    /**
     * Przywraca stan symulacji z memento.
     */
    public static SimulationArea restoreFromMemento(SimulationMemento memento) {
        SimulationArea area = new SimulationArea(memento.getParameters());

        // Wyczyść obecną populację
        area.getPeople().clear();

        // Odtwórz osobników z memento
        for (PersonMemento pm : memento.getPersonStates()) {
            Person person = new Person(pm.getPosX(), pm.getPosY(), pm.getState());
            person.setVelocity(new Vector3DDecorator(
                    new Vector2D(pm.getVelX(), pm.getVelY()), 0));
            area.getPeople().add(person);
        }

        // Przywróć czas symulacji i statystyki poprzez refleksję lub dodatkowe metody
        // (dla uproszczenia pominiemy ustawianie czasu - możesz to rozszerzyć)

        // Przywróć kontrolery
        area.restoreTransientFields();

        return area;
    }

    /**
     * Zapisuje stan symulacji do pliku.
     */
    public static String saveSimulation(SimulationArea area) throws IOException {
        SimulationMemento memento = createMemento(area);
        String filename = SimulationMemento.generateFilename();
        memento.saveToFile(filename);
        return filename;
    }

    /**
     * Wczytuje stan symulacji z pliku.
     */
    public static SimulationArea loadSimulation(String filename)
            throws IOException, ClassNotFoundException {
        SimulationMemento memento = SimulationMemento.loadFromFile(filename);
        return restoreFromMemento(memento);
    }
}