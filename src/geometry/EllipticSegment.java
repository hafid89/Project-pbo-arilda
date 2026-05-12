package geometry;

/**
 * Tembereng elips (3D segment) - bagian dari ellipsoid
 */
public class EllipticSegment extends EllipticSolid {
    private double _segmentHeight; // tinggi segment

    public EllipticSegment(double a, double b, double height, double segmentHeight) {
        super(a, b, height, "Elliptic Segment", "Cyan");
        this._segmentHeight = segmentHeight;
    }

    public EllipticSegment(double a, double b, double height, double segmentHeight, String color) {
        super(a, b, height, "Elliptic Segment", color);
        this._segmentHeight = segmentHeight;
    }

    public double getSegmentHeight() { return _segmentHeight; }
    public void setSegmentHeight(double segmentHeight) { this._segmentHeight = segmentHeight; }

    @Override
    public double computeVolume() {
        // Simplified approximation for segment volume
        double fullVolume = (4.0 / 3.0) * PI * getA() * getB() * getHeight();
        double ratio = _segmentHeight / getHeight();
        return fullVolume * ratio;
    }

    @Override
    public double computeSurfaceArea() {
        // Simplified approximation
        double baseArea = ellipseArea();
        double segmentRatio = _segmentHeight / getHeight();
        double fullSurface = computeFullSurfaceArea();
        return baseArea + fullSurface * segmentRatio;
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