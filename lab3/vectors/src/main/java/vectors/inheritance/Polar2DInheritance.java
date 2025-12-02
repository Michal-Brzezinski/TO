package vectors.inheritance;

import vectors.Vector2D;

/**
 * Alternatywne podejście (Dziedziczenie).
 * Klasa ta JEST wektorem 2D, który DODATKOWO posiada metodę obliczania kąta.
 */
public class Polar2DInheritance extends Vector2D {

    public Polar2DInheritance(double x, double y) {
        super(x, y);
    }

    /**
     * Oblicza kąt między wektorem a dodatnią półosią OX.
     * Realizuje to za pomocą funkcji cyklometrycznej (arcus tangens 2),
     * która poprawnie obsługuje wszystkie ćwiartki układu współrzędnych
     * i zwraca kąt w radianach, który następnie konwertujemy na stopnie.
     * * @return double - kąt w stopniach (-180 do 180)
     */
    public double getAngle() {
        // Math.atan2(y, x) to funkcja cyklometryczna (arcus tangens 2).
        // Jest to dokładnie to, czego wymaga polecenie: obliczenie kąta
        // na podstawie definicji funkcji trygonometrycznych (tg(a) = y/x)
        // z uwzględnieniem kierunku wektora (znaków x i y).
        double angleRad = Math.atan2(getY(), getX());
        return Math.toDegrees(angleRad);
    }

    // Metoda abs() jest po prostu dziedziczona z Vector2D i nie wymaga zmian.
    // public double abs() { return super.abs(); }
}