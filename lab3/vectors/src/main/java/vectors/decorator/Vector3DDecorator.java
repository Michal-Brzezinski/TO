package vectors.decorator;

import vectors.Vector2D;
import vectors.IVector;

/**
 * Dekorator (wzorzec projektowy).
 * "Dekoruje" dowolny IVector, nadając mu nową (lub nadpisując istniejącą)
 * współrzędną 'z' oraz dodając metodę iloczynu wektorowego cross().
 */
public class Vector3DDecorator implements IVector {

    private IVector srcVector; // Obiekt dekorowany
    private double z;

    public Vector3DDecorator(IVector srcVector, double z) {
        this.srcVector = srcVector;
        this.z = z;
    }

    /**
     * Metoda specyficzna dla Dekoratora, zwraca obiekt dekorowany.
     */
    public IVector getSrcV() {
        return srcVector;
    }

    @Override
    public double[] getComponents() {
        // Bierzemy [x, y] z obiektu dekorowanego i dodajemy 'z' tego dekoratora
        double[] srcComps = srcVector.getComponents();
        return new double[]{srcComps[0], srcComps[1], this.z};
    }

    @Override
    public IVector add(IVector other) {
        double[] c1 = this.getComponents();
        double[] c2 = other.getComponents();
        return new Vector3DDecorator(
                new Vector2D(c1[0] + c2[0], c1[1] + c2[1]),
                c1[2] + c2[2]
        );
    }

    @Override
    public IVector subtract(IVector other) {
        double[] c1 = this.getComponents();
        double[] c2 = other.getComponents();
        return new Vector3DDecorator(
                new Vector2D(c1[0] - c2[0], c1[1] - c2[1]),
                c1[2] - c2[2]
        );
    }

    @Override
    public double abs() {
        // Obliczamy moduł na podstawie nowych komponentów [x, y, z]
        double[] c = this.getComponents();
        return Math.sqrt(c[0] * c[0] + c[1] * c[1] + c[2] * c[2]);
    }

    @Override
    public double cdot(IVector param) {
        // Obliczamy iloczyn skalarny na podstawie nowych komponentów [x, y, z]
        double[] c1 = this.getComponents();
        double[] c2 = param.getComponents(); // [x2, y2, z2]

        return c1[0] * c2[0] + c1[1] * c2[1] + c1[2] * c2[2];
    }

    /**
     * Nowa funkcjonalność: Iloczyn wektorowy (metoda macierzowa).
     * v1 x v2 = | i   j   k  |
     *           | x1  y1  z1 |
     *           | x2  y2  z2 |
     * @param param Drugi wektor (IVector)
     * @return Nowy wektor 3D (jako IVector)
     */
    public IVector cross(IVector param) {
        double[] c1 = this.getComponents(); // [x1, y1, z1]
        double[] c2 = param.getComponents(); // [x2, y2, z2]


        // obliczenie współrzędnych na podstawie wyznacznika z wersorami i, j, k:
        // x3 = y1*z2 - z1*y2
        double x3 = c1[1] * c2[2] - c1[2] * c2[1];
        // y3 = z1*x2 - x1*z2
        double y3 = c1[2] * c2[0] - c1[0] * c2[2];
        // z3 = x1*y2 - y1*x2
        double z3 = c1[0] * c2[1] - c1[1] * c2[0];

        // Zwracamy nowy wektor 3D.
        // Zgodnie ze wzorcem, tworzymy nowy Vector2D(x3, y3)
        // i dekorujemy go wartością z3.
        return new Vector3DDecorator(new Vector2D(x3, y3), z3);
    }

    @Override
    public String toString() {
        double[] c = this.getComponents();
        return "(" + c[0] + ", " + c[1] + ", " + c[2] + ")";
    }

}