package geometry;

import exceptions.GeometryException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Kelas Elips - Benda 2 Dimensi
 * Demonstrasi Inheritance, Overriding, dan Encapsulation
 */
public class Elips extends Benda2Dimensi {
    
    private double sumbuPanjang;  // sumbu mayor (a)
    private double sumbuPendek;   // sumbu minor (b)
    private static final double PI = Math.PI;
    
    // Constructor overloading
    public Elips() {
        this(1.0, 1.0);
    }
    
    public Elips(double sumbuPanjang, double sumbuPendek) {
        super("Elips", "Biru");
        this.sumbuPanjang = sumbuPanjang;
        this.sumbuPendek = sumbuPendek;
        hitungLuas();
        hitungKeliling();
    }
    
    public Elips(double sumbuPanjang, double sumbuPendek, String warna) {
        super("Elips", warna);
        this.sumbuPanjang = sumbuPanjang;
        this.sumbuPendek = sumbuPendek;
        hitungLuas();
        hitungKeliling();
    }
    
    // Getter dan Setter (Encapsulation)
    public double getSumbuPanjang() {
        return sumbuPanjang;
    }
    
    public void setSumbuPanjang(double sumbuPanjang) throws GeometryException {
        if (sumbuPanjang <= 0) {
            throw new GeometryException("Sumbu panjang harus > 0", GeometryException.NEGATIVE_VALUE);
        }
        this.sumbuPanjang = sumbuPanjang;
        hitungLuas();
        hitungKeliling();
    }
    
    public double getSumbuPendek() {
        return sumbuPendek;
    }
    
    public void setSumbuPendek(double sumbuPendek) throws GeometryException {
        if (sumbuPendek <= 0) {
            throw new GeometryException("Sumbu pendek harus > 0", GeometryException.NEGATIVE_VALUE);
        }
        this.sumbuPendek = sumbuPendek;
        hitungLuas();
        hitungKeliling();
    }
    
    @Override
    public double hitungLuas() {
        // Luas elips = π × a × b
        luas = PI * sumbuPanjang * sumbuPendek;
        return luas;
    }
    
    @Override
    public double hitungKeliling() {
        // Rumus pendekatan Ramanujan untuk keliling elips
        double a = sumbuPanjang;
        double b = sumbuPendek;
        keliling = PI * (3 * (a + b) - Math.sqrt((3 * a + b) * (a + 3 * b)));
        return keliling;
    }
    
    public double hitungEksentrisitas() {
        return Math.sqrt(1 - Math.pow(sumbuPendek / sumbuPanjang, 2));
    }
    
    @Override
    public String info() {
        return String.format("""
            === %s ===
            Warna: %s
            Sumbu Panjang (a): %.4f
            Sumbu Pendek (b): %.4f
            Luas: %.4f satuan luas
            Keliling: %.4f satuan panjang
            Eksentrisitas: %.4f
            """, getNama(), getWarna(), sumbuPanjang, sumbuPendek, luas, keliling, hitungEksentrisitas());
    }
    
    public String toCSVFormat() {
        return String.format("Elips,%.4f,%.4f,%.4f,%.4f", sumbuPanjang, sumbuPendek, luas, keliling);
    }
    
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(info());
        }
    }
}