package simulation.memento;

import simulation.model.Person;
import simulation.util.SimulationParameters;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa przechowująca stan symulacji (wzorzec Pamiątka).
 */
public class SimulationMemento implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<PersonMemento> personStates;
    private final double simulationTime;
    private final SimulationParameters parameters;
    private final int totalSpawned;
    private final int totalRemoved;
    private final long timestamp;

    public SimulationMemento(List<Person> people, double simulationTime,
                             SimulationParameters parameters, int totalSpawned, int totalRemoved) {
        this.personStates = new ArrayList<>();

        // Zapisz stan każdego osobnika
        for (Person person : people) {
            personStates.add(new PersonMemento(person));
        }

        this.simulationTime = simulationTime;
        this.parameters = parameters;
        this.totalSpawned = totalSpawned;
        this.totalRemoved = totalRemoved;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Zapisuje memento do pliku.
     * @param filename Nazwa pliku
     */
    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    /**
     * Wczytuje memento z pliku.
     * @param filename Nazwa pliku
     * @return Wczytane memento
     */
    public static SimulationMemento loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return (SimulationMemento) ois.readObject();
        }
    }

    /**
     * Generuje nazwę pliku z timestampem.
     */
    public static String generateFilename() {
        return "simulation_" + System.currentTimeMillis() + ".sav";
    }

    // Gettery

    public List<PersonMemento> getPersonStates() {
        return personStates;
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public SimulationParameters getParameters() {
        return parameters;
    }

    public int getTotalSpawned() {
        return totalSpawned;
    }

    public int getTotalRemoved() {
        return totalRemoved;
    }

    public long getTimestamp() {
        return timestamp;
    }
}