package geometry;

/**
 * Tabung dengan alas elips
 */
public class Cylinder extends EllipticSolid {

    public Cylinder(double a, double b, double height) {
        super(a, b, height, "Elliptic Cylinder", "Green");
    }

    public Cylinder(double a, double b, double height, String color) {
        super(a, b, height, "Elliptic Cylinder", color);
    }

    @Override
    public double computeVolume() {
        double baseArea = ellipseArea();
        return baseArea * getHeight();
    }

    @Override
    public double computeSurfaceArea() {
        double baseArea = ellipseArea();
        double lateralArea;

        if (isCircle()) {
            // Exact for circular cylinder
            lateralArea = 2 * PI * getA() * getHeight();
        } else {
            // Approximation for elliptic cylinder
            lateralArea = PI * (getA() + getB()) * getHeight();
        }

        return 2 * baseArea + lateralArea;
    }
}