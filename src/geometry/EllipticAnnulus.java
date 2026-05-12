package geometry;

/**
 * Cincin elips (annulus / torus-like)
 */
public class EllipticAnnulus extends EllipticSolid {
    private double _innerA; // sumbu panjang dalam
    private double _innerB; // sumbu pendek dalam

    public EllipticAnnulus(double outerA, double outerB, double innerA, double innerB, double height) {
        super(outerA, outerB, height, "Elliptic Annulus", "Pink");
        this._innerA = innerA;
        this._innerB = innerB;
    }

    public EllipticAnnulus(double outerA, double outerB, double innerA, double innerB, double height, String color) {
        super(outerA, outerB, height, "Elliptic Annulus", color);
        this._innerA = innerA;
        this._innerB = innerB;
    }

    public double getInnerA() { return _innerA; }
    public double getInnerB() { return _innerB; }
    public void setInnerA(double innerA) { this._innerA = innerA; }
    public void setInnerB(double innerB) { this._innerB = innerB; }

    private double innerEllipseArea() {
        return PI * _innerA * _innerB;
    }

    @Override
    public double computeVolume() {
        double outerArea = ellipseArea();
        double innerArea = innerEllipseArea();
        return (outerArea - innerArea) * getHeight();
    }

    @Override
    public double computeSurfaceArea() {
        double outerArea = ellipseArea();
        double innerArea = innerEllipseArea();
        double lateralArea;

        if (isCircle() && Math.abs(_innerA - _innerB) < 1e-10) {
            // Exact for circular annulus
            lateralArea = 2 * PI * getHeight() * (getA() + _innerA);
        } else {
            // Approximation for elliptic annulus
            lateralArea = PI * getHeight() * ((getA() + getB()) + (_innerA + _innerB));
        }

        return 2 * (outerArea - innerArea) + lateralArea;
    }
}