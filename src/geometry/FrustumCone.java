package geometry;

/**
 * Kerucut terpancung dengan alas elips
 */
public class FrustumCone extends EllipticSolid {
    private double _topA; // sumbu panjang atas
    private double _topB; // sumbu pendek atas

    public FrustumCone(double bottomA, double bottomB, double topA, double topB, double height) {
        super(bottomA, bottomB, height, "Elliptic Frustum Cone", "Orange");
        this._topA = topA;
        this._topB = topB;
    }

    public FrustumCone(double bottomA, double bottomB, double topA, double topB, double height, String color) {
        super(bottomA, bottomB, height, "Elliptic Frustum Cone", color);
        this._topA = topA;
        this._topB = topB;
    }

    public double getTopA() { return _topA; }
    public double getTopB() { return _topB; }
    public void setTopA(double topA) { this._topA = topA; }
    public void setTopB(double topB) { this._topB = topB; }

    private boolean isTopCircle() {
        return Math.abs(_topA - _topB) < 1e-10;
    }

    private double topEllipseArea() {
        return PI * _topA * _topB;
    }

    private double topEllipsePerimeterApprox() {
        return PI * (_topA + _topB);
    }

    @Override
    public double computeVolume() {
        double bottomArea = ellipseArea();
        double topArea = topEllipseArea();
        return (getHeight() / 3.0) * (bottomArea + topArea + Math.sqrt(bottomArea * topArea));
    }

    @Override
    public double computeSurfaceArea() {
        double bottomArea = ellipseArea();
        double topArea = topEllipseArea();
        double slantHeight = slantHeight();
        double lateralArea;

        if (isCircle() && isTopCircle()) {
            // Exact for circular frustum
            lateralArea = PI * (getA() + _topA) * slantHeight;
        } else {
            // Approximation for elliptic frustum
            lateralArea = PI * ((getA() + getB()) + (_topA + _topB)) * slantHeight;
        }

        return bottomArea + topArea + lateralArea;
    }
}