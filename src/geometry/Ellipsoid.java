package geometry;

/**
 * Ellipsoid (bola elips)
 */
public class Ellipsoid extends EllipticSolid {

    public Ellipsoid(double a, double b, double c) {
        super(a, b, c, "Ellipsoid", "Purple");
    }

    public Ellipsoid(double a, double b, double c, String color) {
        super(a, b, c, "Ellipsoid", color);
    }

    @Override
    public double computeVolume() {
        return (4.0 / 3.0) * PI * getA() * getB() * getHeight();
    }

    @Override
    public double computeSurfaceArea() {
        // Approximation for ellipsoid surface area
        double a = getA();
        double b = getB();
        double c = getHeight();

        if (Math.abs(a - b) < 1e-10 && Math.abs(b - c) < 1e-10) {
            // Exact for sphere
            return 4 * PI * a * a;
        } else {
            // Knud Thomsen's approximation for triaxial ellipsoid
            double p = 1.6075;
            double ap = Math.pow(a, p);
            double bp = Math.pow(b, p);
            double cp = Math.pow(c, p);
            return 4 * PI * Math.pow((ap * bp + ap * cp + bp * cp) / 3.0, 1.0 / p);
        }
    }
}