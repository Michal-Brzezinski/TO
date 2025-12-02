package vectors.adapter;

/**
 * Interfejs dla wektora w układzie biegunowym 2D.
 */
public interface IPolar2D {
    /**
     * Zwraca kąt wektora względem osi OX (w stopniach).
     * @return double - kąt w stopniach
     */
    double getAngle();

    /**
     * Zwraca moduł (promień) wektora.
     * @return double - moduł
     */
    double abs();
}