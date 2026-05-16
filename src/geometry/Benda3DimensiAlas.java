package geometry;

/**
 * Abstract class untuk benda 3 dimensi yang memiliki alas
 * Demonstrasi Inheritance dan Polymorphism
 */
public abstract class Benda3DimensiAlas extends Benda3Dimensi {
    protected Benda2Dimensi alas;

    public Benda3DimensiAlas(String nama, String warna, Benda2Dimensi alas) {
        super(nama, warna);
        this.alas = alas;
    }

    public Benda3DimensiAlas(String nama, Benda2Dimensi alas) {
        this(nama, "Putih", alas);
    }

    public synchronized Benda2Dimensi getAlas() {
        return alas;
    }

    public synchronized void setAlas(Benda2Dimensi alas) {
        this.alas = alas;
    }

    public synchronized boolean hasAlas() {
        return alas != null;
    }

    public synchronized double hitungLuasAlas() {
        return hasAlas() ? alas.hitungLuas() : 0;
    }

    public synchronized double hitungKelilingAlas() {
        return hasAlas() ? alas.hitungKeliling() : 0;
    }
}