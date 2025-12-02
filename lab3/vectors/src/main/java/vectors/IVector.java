package vectors;

/**
 * Interfejs bazowy dla wektora.
 * Zgodnie z uwagami, wszystkie operacje zakładają możliwość istnienia 3 wymiarów.
 */

public interface IVector {

    /**
     * Dodaje do tego wektora inny wektor.
     * @param other Drugi wektor
     * @return Nowy wektor będący sumą
     */
    IVector add(IVector other);

    /**
     * Odejmuje od tego wektora inny wektor.
     * @param other Drugi wektor
     * @return Nowy wektor będący różnicą
     */
    IVector subtract(IVector other);

    /**
     * Oblicza moduł (długość) wektora.
     * @return double - moduł wektora
     */
    double abs();

    /**
     * Oblicza iloczyn skalarny tego wektora przez inny.
     * @param param Drugi wektor (IVector)
     * @return double - wynik iloczynu skalarnego
     */
    double cdot(IVector param);

    /**
     * Zwraca współrzędne kartezjańskie wektora.
     * Zgodnie z uwagami, standaryzujemy do 3 wymiarów.
     * @return tablica double[3] w formacie [x, y, z]
     */
    double[] getComponents();
}