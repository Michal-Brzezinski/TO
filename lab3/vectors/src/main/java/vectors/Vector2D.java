package vectors;

/**
 * Konkretna implementacja wektora 2D.
 * Implementuje IVector, zakładając z=0 zgodnie z poleceniem.
 */
public class Vector2D implements IVector {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Gettery potrzebne dla Adaptera
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public IVector add(IVector other) {
        double[] c1 = this.getComponents();
        double[] c2 = other.getComponents();
        return new Vector2D(c1[0] + c2[0], c1[1] + c2[1]);
    }

    @Override
    public IVector subtract(IVector other) {
        double[] c1 = this.getComponents();
        double[] c2 = other.getComponents();
        return new Vector2D(c1[0] - c2[0], c1[1] - c2[1]);
    }

    @Override
    public double abs() {
        // Moduł wektora [x, y, 0] to sqrt(x^2 + y^2 + 0^2)
        return Math.hypot(this.x, this.y);
    }

    @Override
    public double cdot(IVector param) {
        double[] c1 = this.getComponents(); // [x, y, 0]
        double[] c2 = param.getComponents(); // [x2, y2, z2]

        // v1 · v2 = x1*x2 + y1*y2 + z1*z2
        // Ponieważ c1[2] (nasze z) jest zawsze 0, składnik zniknie
        return c1[0] * c2[0] + c1[1] * c2[1] + c1[2] * c2[2];
    }

    @Override
    public double[] getComponents() {
        // Zgodnie z uwagą, dla 2D zwracamy [x, y, 0]
        return new double[]{this.x, this.y, 0.0};
    }
}