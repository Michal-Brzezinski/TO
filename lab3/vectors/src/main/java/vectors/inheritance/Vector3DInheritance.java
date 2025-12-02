package vectors.inheritance;

import vectors.Vector2D;
import vectors.IVector;

/**
 * Alternatywne podejście (Dziedziczenie).
 * Klasa ta JEST wektorem 3D, który rozszerza Vector2D.
 */
public class Vector3DInheritance extends Vector2D {

    private double z;

    public Vector3DInheritance(double x, double y, double z) {
        super(x, y); // Wywołanie konstruktora klasy bazowej (Vector2D)
        this.z = z;
    }

    /**
     * Zgodnie z opisem diagramu, "tworzy nowy obiekt" 2D.
     */
    public IVector getSrcV() {
        return new Vector2D(getX(), getY());
    }

    @Override
    public double[] getComponents() {
        // Nadpisuje metodę bazową, aby uwzględnić 'z'
        return new double[]{getX(), getY(), this.z};
    }

    @Override
    public double abs() {
        // Nadpisuje metodę bazową, aby uwzględnić 'z'
        double[] c = this.getComponents();
        return Math.sqrt(c[0] * c[0] + c[1] * c[1] + c[2] * c[2]);
    }

    @Override
    public double cdot(IVector param) {
        // Nadpisuje metodę bazową, aby uwzględnić 'z'
        double[] c1 = this.getComponents();
        double[] c2 = param.getComponents();

        return c1[0] * c2[0] + c1[1] * c2[1] + c1[2] * c2[2];
        // można niby         return this.getX() * c2[0] + this.getY() * c2[1] + this.z * c2[2];
        // ale teraz jest raczej bardziej czytelne
    }

    /**
     * Nowa funkcjonalność: Iloczyn wektorowy.
     */
    public IVector cross(IVector param) {
        double[] c1 = this.getComponents();
        double[] c2 = param.getComponents();

        // obliczenie współrzędnych na podstawie wyznacznika z wersorami i, j, k
        double x3 = c1[1] * c2[2] - c1[2] * c2[1];
        double y3 = c1[2] * c2[0] - c1[0] * c2[2];
        double z3 = c1[0] * c2[1] - c1[1] * c2[0];

        // Zwraca nowy wektor tego samego typu
        return new Vector3DInheritance(x3, y3, z3);
    }
}