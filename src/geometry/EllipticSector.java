package geometry;

/**
 * Juring elips (3D sector) - bagian dari ellipsoid
 */
public class EllipticSector extends EllipticSolid {
    private double _angle; // sudut dalam radian

    public EllipticSector(double a, double b, double height, double angle) {
        super(a, b, height, "Elliptic Sector", "Yellow");
        this._angle = angle;
    }

    public EllipticSector(double a, double b, double height, double angle, String color) {
        super(a, b, height, "Elliptic Sector", color);
        this._angle = angle;
    }

    public double getAngle() { return _angle; }
    public void setAngle(double angle) { this._angle = angle; }

    @Override
    public double computeVolume() {
        double fullVolume = (4.0 / 3.0) * PI * getA() * getB() * getHeight();
        return fullVolume * (_angle / (2 * PI));
    }

    @Override
    public double computeSurfaceArea() {
        // Simplified approximation
        double fullSurface = computeFullSurfaceArea();
        double sectorRatio = _angle / (2 * PI);
        return fullSurface * sectorRatio + ellipseArea() * (1 - sectorRatio); // rough approximation
    }

    private double computeFullSurfaceArea() {
        double a = getA();
        double b = getB();
        double c = getHeight();
        double p = 1.6075;
        double ap = Math.pow(a, p);
        double bp = Math.pow(b, p);
        double cp = Math.pow(c, p);
        return 4 * PI * Math.pow((ap * bp + ap * cp + bp * cp) / 3.0, 1.0 / p);
    }
}