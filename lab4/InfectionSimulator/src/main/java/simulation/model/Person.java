package simulation.model;

import simulation.state.*;
import vectors.IVector;
import vectors.Vector2D;
import vectors.decorator.Vector3DDecorator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Klasa reprezentująca osobnika w symulacji.
 */
public class Person implements Serializable {

    private IVector position;  // Pozycja (x, y, z=0)
    private IVector velocity;  // Prędkość (vx, vy, vz=0)
    private PersonState currentState;

    // Śledzenie ekspozycji na zakażonych
    private Map<Person, Double> exposureTimers; // Osoba -> czas ekspozycji

    // Identyfikator osobnika (do debugowania)
    private static int idCounter = 0;
    private final int id;

    public Person(double x, double y, PersonState initialState) {
        this.position = new Vector3DDecorator(new Vector2D(x, y), 0);
        this.velocity = new Vector3DDecorator(new Vector2D(0, 0), 0);
        this.currentState = initialState;
        this.exposureTimers = new HashMap<>();
        this.id = idCounter++;
    }

    /**
     * Aktualizuje stan osobnika.
     * @param deltaTime Czas od ostatniej aktualizacji (sekundy)
     */
    public void update(double deltaTime) {
        // Aktualizuj stan (np. licznik zakażenia)
        currentState.update(this, deltaTime);

        // Aktualizuj pozycję na podstawie prędkości
        // position = position + velocity * deltaTime
        IVector displacement = multiplyVector(velocity, deltaTime);
        position = position.add(displacement);
    }

    /**
     * Mnoży wektor przez skalar.
     */
    private IVector multiplyVector(IVector v, double scalar) {
        double[] c = v.getComponents();
        return new Vector3DDecorator(
                new Vector2D(c[0] * scalar, c[1] * scalar),
                c[2] * scalar
        );
    }

    /**
     * Ustawia losową prędkość.
     * @param maxSpeed Maksymalna prędkość (m/s)
     */
    public void setRandomVelocity(double maxSpeed) {
        // Losujemy kąt (0-360 stopni)
        double angle = Math.random() * 2 * Math.PI;
        // Losujemy prędkość (0 - maxSpeed)
        double speed = Math.random() * maxSpeed;

        double vx = speed * Math.cos(angle);
        double vy = speed * Math.sin(angle);

        this.velocity = new Vector3DDecorator(new Vector2D(vx, vy), 0);
    }

    /**
     * Zmienia kierunek ruchu losowo.
     * @param maxSpeed Maksymalna prędkość
     */
    public void randomlyChangeDirection(double maxSpeed) {
        setRandomVelocity(maxSpeed);
    }

    /**
     * Obsługuje kolizję z granicą obszaru.
     * @param width Szerokość obszaru (metry)
     * @param height Wysokość obszaru (metry)
     * @param returnProbability Prawdopodobieństwo zawrócenia (zamiast wyjścia)
     * @return true jeśli osobnik powinien zostać usunięty, false w przeciwnym razie
     */
    public boolean handleBoundaryCollision(int width, int height, double returnProbability) {
        double[] pos = position.getComponents();
        double x = pos[0];
        double y = pos[1];

        boolean hitBoundary = false;

        // Sprawdź kolizję z lewą/prawą granicą
        if (x < 0 || x > width) {
            hitBoundary = true;
        }

        // Sprawdź kolizję z górną/dolną granicą
        if (y < 0 || y > height) {
            hitBoundary = true;
        }

        if (hitBoundary) {
            if (Math.random() < returnProbability) {
                // Zawróć - odbij prędkość
                double[] vel = velocity.getComponents();

                // Odbij w osi X jeśli wyszedł poza lewo/prawo
                if (x < 0 || x > width) {
                    vel[0] = -vel[0];
                    // Przesuń z powrotem do obszaru
                    if (x < 0) pos[0] = 0.1;
                    if (x > width) pos[0] = width - 0.1;
                }

                // Odbij w osi Y jeśli wyszedł poza górę/dół
                if (y < 0 || y > height) {
                    vel[1] = -vel[1];
                    // Przesuń z powrotem do obszaru
                    if (y < 0) pos[1] = 0.1;
                    if (y > height) pos[1] = height - 0.1;
                }

                velocity = new Vector3DDecorator(new Vector2D(vel[0], vel[1]), 0);
                position = new Vector3DDecorator(new Vector2D(pos[0], pos[1]), 0);

                return false; // Nie usuwaj
            } else {
                // Opuść obszar - usuń osobnika
                return true;
            }
        }

        return false; // Nie usuwaj
    }

    /**
     * Aktualizuje timer ekspozycji na innego osobnika.
     * @param infectedPerson Zakażony osobnik
     * @param deltaTime Czas ekspozycji w tym kroku
     */
    public void updateExposure(Person infectedPerson, double deltaTime) {
        exposureTimers.merge(infectedPerson, deltaTime, Double::sum);
    }

    /**
     * Sprawdza czy ekspozycja na danego osobnika przekroczyła próg zakażenia.
     * @param infectedPerson Zakażony osobnik
     * @param threshold Próg czasowy (sekundy)
     * @return true jeśli czas ekspozycji >= threshold
     */
    public boolean hasExceededExposureThreshold(Person infectedPerson, double threshold) {
        return exposureTimers.getOrDefault(infectedPerson, 0.0) >= threshold;
    }

    /**
     * Resetuje timer ekspozycji dla danego osobnika.
     */
    public void resetExposureTimer(Person person) {
        exposureTimers.remove(person);
    }

    /**
     * Czyści wszystkie timery ekspozycji dla osobników, którzy są poza zasięgiem.
     */
    public void clearExposureTimers() {
        exposureTimers.clear();
    }

    /**
     * Resetuje licznik ID (do resetu symulacji).
     */
    public static void resetIdCounter() {
        idCounter = 0;
    }

    // Gettery i settery

    public IVector getPosition() {
        return position;
    }

    public void setPosition(IVector position) {
        this.position = position;
    }

    public IVector getVelocity() {
        return velocity;
    }

    public void setVelocity(IVector velocity) {
        this.velocity = velocity;
    }

    public PersonState getCurrentState() {
        return currentState;
    }

    public void setState(PersonState newState) {
        this.currentState = newState;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        double[] pos = position.getComponents();
        return String.format("Person[id=%d, pos=(%.2f, %.2f), state=%s]",
                id, pos[0], pos[1], currentState.getName());
    }
}