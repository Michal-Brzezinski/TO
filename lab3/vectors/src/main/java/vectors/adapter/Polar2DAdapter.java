package vectors.adapter;

import vectors.Vector2D;
import vectors.IVector;

/**
 * Adapter (wzorzec projektowy).
 * Adaptuje klasę Vector2D do interfejsu IPolar2D.
 * Jednocześnie implementuje IVector przez delegację,
 * aby mógł być używany wymiennie z Vector2D.
 */
public class Polar2DAdapter implements IPolar2D, IVector {

    private Vector2D srcVector; // Obiekt adaptowany

    public Polar2DAdapter(Vector2D srcVector) {
        this.srcVector = srcVector;
    }

    // --- Implementacja nowego interfejsu IPolar2D ---

    @Override
    public double getAngle() {
        // Używamy Math.atan2 do obliczenia kąta w radianach (poprawnie obsługuje ćwiartki)
        // i konwertujemy na stopnie.
        double angleRad = Math.atan2(srcVector.getY(), srcVector.getX());
        return Math.toDegrees(angleRad);
    }

    // --- Implementacja interfejsu IVector przez delegację ---

    @Override
    public IVector add(IVector other) {
        return srcVector.add(other);
    }

    @Override
    public IVector subtract(IVector other) {
        return srcVector.subtract(other);
    }

    @Override
    public double abs() {
        // Delegujemy wywołanie do adaptowanego obiektu
        return srcVector.abs();
    }

    @Override
    public double cdot(IVector param) {
        // Delegujemy wywołanie do adaptowanego obiektu
        return srcVector.cdot(param);
    }

    @Override
    public double[] getComponents() {
        // Delegujemy wywołanie do adaptowanego obiektu
        return srcVector.getComponents();
    }
}