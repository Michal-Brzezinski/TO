package simulation.memento;

import simulation.model.Person;
import simulation.state.PersonState;
import vectors.IVector;

import java.io.Serializable;

/**
 * Klasa przechowująca stan pojedynczego osobnika.
 */
public class PersonMemento implements Serializable {

    private static final long serialVersionUID = 1L;

    private final double posX;
    private final double posY;
    private final double velX;
    private final double velY;
    private final PersonState state;
    private final int id;

    public PersonMemento(Person person) {
        double[] pos = person.getPosition().getComponents();
        this.posX = pos[0];
        this.posY = pos[1];

        double[] vel = person.getVelocity().getComponents();
        this.velX = vel[0];
        this.velY = vel[1];

        this.state = person.getCurrentState();
        this.id = person.getId();
    }

    // Gettery

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getVelX() {
        return velX;
    }

    public double getVelY() {
        return velY;
    }

    public PersonState getState() {
        return state;
    }

    public int getId() {
        return id;
    }
}