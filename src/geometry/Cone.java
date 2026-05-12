package geometry;

/**
 * Kerucut dengan alas elips
 * Menggunakan method overriding untuk computeVolume dan computeSurfaceArea
 */
public class Cone extends EllipticSolid {

    public Cone(double a, double b, double height) {
        super(a, b, height, "Elliptic Cone", "Red");
    }

    public Cone(double a, double b, double height, String color) {
        super(a, b, height, "Elliptic Cone", color);
    }

    @Override
    public double computeVolume() {
        double baseArea = ellipseArea();
        return (1.0 / 3.0) * baseArea * getHeight();
    }

    @Override
    public double computeSurfaceArea() {
        double baseArea = ellipseArea();
        double slantHeight = slantHeight();
        double lateralArea;

        if (isCircle()) {
            // Exact for circle
            lateralArea = PI * getA() * slantHeight;
        } else {
            // Approximation for ellipse
            lateralArea = PI * (getA() + getB()) * slantHeight;
        }

        return baseArea + lateralArea;
    }
}