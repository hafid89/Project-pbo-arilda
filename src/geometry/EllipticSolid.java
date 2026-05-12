package geometry;

/**
 * Base class untuk semua bangun 3D berbasis elips
 * Menggunakan encapsulation dengan private attributes
 * Mendukung inheritance, polymorphism, method overriding
 */
public abstract class EllipticSolid {
    // Encapsulation: private attributes
    private double _a; // sumbu panjang elips
    private double _b; // sumbu pendek elips
    private double _height; // tinggi bangun
    private String _name;
    private String _color;

    // Static constant
    protected static final double PI = Math.PI;

    // Constructor
    public EllipticSolid(double a, double b, double height, String name, String color) {
        this._a = a;
        this._b = b;
        this._height = height;
        this._name = name;
        this._color = color;
    }

    // Getters (Information Hiding)
    public double getA() { return _a; }
    public double getB() { return _b; }
    public double getHeight() { return _height; }
    public String getName() { return _name; }
    public String getColor() { return _color; }

    // Setters
    public void setA(double a) { this._a = a; }
    public void setB(double b) { this._b = b; }
    public void setHeight(double height) { this._height = height; }
    public void setName(String name) { this._name = name; }
    public void setColor(String color) { this._color = color; }

    // Helper methods
    protected boolean isCircle() {
        return Math.abs(_a - _b) < 1e-10; // floating point comparison
    }

    protected double ellipseArea() {
        return PI * _a * _b;
    }

    protected double ellipsePerimeterApprox() {
        return PI * (_a + _b);
    }

    protected double radiusAverage() {
        return (_a + _b) / 2.0;
    }

    protected double slantHeight() {
        if (isCircle()) {
            return Math.sqrt(_a * _a + _height * _height);
        } else {
            double s1 = Math.sqrt(_a * _a + _height * _height);
            double s2 = Math.sqrt(_b * _b + _height * _height);
            return (s1 + s2) / 2.0;
        }
    }

    // Abstract methods for overriding
    public abstract double computeVolume();
    public abstract double computeSurfaceArea();

    // Polymorphic method
    public String getInfo() {
        return String.format("""
            === %s ===
            Color: %s
            Semi-major axis (a): %.4f
            Semi-minor axis (b): %.4f
            Height: %.4f
            Volume: %.4f
            Surface Area: %.4f
            """, _name, _color, _a, _b, _height, computeVolume(), computeSurfaceArea());
    }
}