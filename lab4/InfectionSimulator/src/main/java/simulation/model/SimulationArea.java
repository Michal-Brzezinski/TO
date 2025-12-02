package simulation.model;

import simulation.controller.InfectionController;
import simulation.controller.MovementController;
import simulation.state.*;
import simulation.util.SimulationParameters;
import vectors.Vector2D;
import vectors.decorator.Vector3DDecorator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Główna klasa zarządzająca obszarem symulacji.
 */
public class SimulationArea implements Serializable {

    private final SimulationParameters params;
    private List<Person> people;
    private double simulationTime;
    private Random random;

    // Kontrolery
    private transient MovementController movementController;
    private transient InfectionController infectionController;

    // Statystyki
    private int totalSpawned;
    private int totalRemoved;

    public SimulationArea(SimulationParameters params) {
        this.params = params;
        this.people = new ArrayList<>();
        this.simulationTime = 0.0;
        this.random = new Random();
        this.totalSpawned = 0;
        this.totalRemoved = 0;

        initializeControllers();
        initializePopulation();
    }

    /**
     * Inicjalizuje kontrolery (potrzebne po deserializacji).
     */
    private void initializeControllers() {
        this.movementController = new MovementController(params);
        this.infectionController = new InfectionController(params);
    }

    /**
     * Inicjalizuje początkową populację.
     */
    private void initializePopulation() {
        for (int i = 0; i < params.initialPopulation; i++) {
            Person person = createRandomPerson();

            // Pierwsze N osób to zakażeni
            if (i < params.initialInfected) {
                if (random.nextBoolean()) {
                    person.setState(new InfectedAsymptomaticState());
                } else {
                    person.setState(new InfectedSymptomaticState());
                }
            }

            people.add(person);
            totalSpawned++;
        }
    }

    /**
     * Tworzy losowego osobnika w losowym miejscu obszaru.
     */
    private Person createRandomPerson() {
        double x = random.nextDouble() * params.width;
        double y = random.nextDouble() * params.height;

        PersonState initialState;

        // Sprawdź czy ma być odporny
        if (random.nextDouble() < params.immunityProbability) {
            initialState = new ImmuneState();
        } else {
            initialState = new HealthyState();
        }

        Person person = new Person(x, y, initialState);
        person.setRandomVelocity(params.maxSpeed);

        return person;
    }

    /**
     * Tworzy osobnika na granicy obszaru (spawn).
     */
    private Person createPersonAtBoundary() {
        double x, y;

        // Losujemy która granica: 0=góra, 1=prawo, 2=dół, 3=lewo
        int side = random.nextInt(4);

        switch (side) {
            case 0: // Góra
                x = random.nextDouble() * params.width;
                y = 0;
                break;
            case 1: // Prawo
                x = params.width;
                y = random.nextDouble() * params.height;
                break;
            case 2: // Dół
                x = random.nextDouble() * params.width;
                y = params.height;
                break;
            default: // Lewo
                x = 0;
                y = random.nextDouble() * params.height;
                break;
        }

        PersonState initialState;

        // 10% szansy na zakażenie przy wejściu
        if (random.nextDouble() < params.infectionProbabilityOnEntry) {
            if (random.nextBoolean()) {
                initialState = new InfectedAsymptomaticState();
            } else {
                initialState = new InfectedSymptomaticState();
            }
        } else if (random.nextDouble() < params.immunityProbability) {
            // Sprawdź odporność
            initialState = new ImmuneState();
        } else {
            initialState = new HealthyState();
        }

        Person person = new Person(x, y, initialState);

        // Ustaw prędkość skierowaną do wnętrza obszaru
        double angle;
        switch (side) {
            case 0: // Góra - idź w dół
                angle = Math.PI / 2 + (random.nextDouble() - 0.5) * Math.PI / 2;
                break;
            case 1: // Prawo - idź w lewo
                angle = Math.PI + (random.nextDouble() - 0.5) * Math.PI / 2;
                break;
            case 2: // Dół - idź w górę
                angle = 3 * Math.PI / 2 + (random.nextDouble() - 0.5) * Math.PI / 2;
                break;
            default: // Lewo - idź w prawo
                angle = (random.nextDouble() - 0.5) * Math.PI / 2;
                break;
        }

        double speed = random.nextDouble() * params.maxSpeed;
        double vx = speed * Math.cos(angle);
        double vy = speed * Math.sin(angle);
        person.setVelocity(new Vector3DDecorator(new Vector2D(vx, vy), 0));

        return person;
    }

    /**
     * Główna metoda aktualizacji symulacji.
     * @param deltaTime Czas od ostatniej aktualizacji (sekundy)
     */
    public void update(double deltaTime) {
        simulationTime += deltaTime;

        // 1. Aktualizuj ruch wszystkich osobników
        Iterator<Person> iterator = people.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();

            boolean shouldRemove = movementController.updateMovement(person, deltaTime);

            if (shouldRemove) {
                iterator.remove();
                totalRemoved++;
            }
        }

        // 2. Sprawdź zakażenia
        infectionController.processInfections(people, deltaTime);

        // 3. Spawnuj nowych osobników
        // Prawdopodobieństwo spawnu w tym kroku = spawnRate * deltaTime
        if (random.nextDouble() < params.spawnRate * deltaTime) {
            Person newPerson = createPersonAtBoundary();
            people.add(newPerson);
            totalSpawned++;
        }
    }

    /**
     * Resetuje symulację do stanu początkowego.
     */
    public void reset() {
        people.clear();
        simulationTime = 0.0;
        totalSpawned = 0;
        totalRemoved = 0;
        Person.resetIdCounter();
        initializePopulation();
    }

    /**
     * Przywraca kontrolery po deserializacji.
     */
    public void restoreTransientFields() {
        initializeControllers();
    }

    // Gettery dla statystyk

    public int getHealthyCount() {
        return (int) people.stream()
                .filter(p -> p.getCurrentState() instanceof HealthyState)
                .count();
    }

    public int getInfectedCount() {
        return (int) people.stream()
                .filter(p -> p.getCurrentState() instanceof InfectedAsymptomaticState
                        || p.getCurrentState() instanceof InfectedSymptomaticState)
                .count();
    }

    public int getImmuneCount() {
        return (int) people.stream()
                .filter(p -> p.getCurrentState() instanceof ImmuneState)
                .count();
    }

    public int getTotalCount() {
        return people.size();
    }

    // Gettery i settery

    public List<Person> getPeople() {
        return people;
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public SimulationParameters getParams() {
        return params;
    }

    public int getTotalSpawned() {
        return totalSpawned;
    }

    public int getTotalRemoved() {
        return totalRemoved;
    }
}