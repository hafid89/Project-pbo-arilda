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
    protected double volume;
    protected double luasPermukaan;
    private static final double PI = Math.PI;
    
    // Constructor overloading
    public Elips() {
        this(1.0, 1.0);
    }
    
    public Elips(double sumbuPanjang, double sumbuPendek) {
        super("Elips");
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
        double a = sumbuPanjang;
        double b = sumbuPendek;
        
        // Kasus khusus: jika berbentuk lingkaran (a == b)
        if (Math.abs(a - b) < 1e-9) {
            keliling = 2 * PI * a;
            return keliling;
        }
        
        // Rumus Ramanujan
        keliling = PI * (3 * (a + b) - Math.sqrt((3 * a + b) * (a + 3 * b)));
        return keliling;
    }
    
    // untuk child class
    public double hitungVolume() {
        return 0;
    }
    
    public double hitungLuasPermukaan() {
        return hitungLuas();
    }
    
    /**
     * Menghitung keliling dengan rumus Ramanujan yang lebih akurat (opsional)
     * Error < 0.01% untuk semua rasio a/b
     */
    public double hitungKelilingRamanujanAdvanced() {
        double a = sumbuPanjang;
        double b = sumbuPendek;
        
        if (Math.abs(a - b) < 1e-9) {
            return 2 * PI * a;
        }
        
        double h = Math.pow((a - b), 2) / Math.pow((a + b), 2);
        double kelilingAdvanced = PI * (a + b) * (1 + (3 * h) / (10 + Math.sqrt(4 - 3 * h)));
        return kelilingAdvanced;
    }
    
    public double hitungEksentrisitas() {
        if (sumbuPanjang <= 0) return 0;
        return Math.sqrt(1 - Math.pow(sumbuPendek / sumbuPanjang, 2));
    }
    
    @Override
    public String info() {
        return String.format("""
            === %s ===
            Sumbu Panjang (a): %.4f
            Sumbu Pendek (b): %.4f
            Luas: %.4f satuan luas
            Keliling: %.4f satuan panjang
            Eksentrisitas: %.4f
            """, getNama(), sumbuPanjang, sumbuPendek, luas, keliling, hitungEksentrisitas());
    }
    
    public String toCSVFormat() {
        return String.format("Elips,%.4f,%.4f,%.4f,%.4f", sumbuPanjang, sumbuPendek, luas, keliling);
    }
    
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(info());
        }
    }
    
    @Override
    public String toString() {
        return String.format("Elips(a=%.2f, b=%.2f, luas=%.2f, keliling=%.2f)", 
                            sumbuPanjang, sumbuPendek, luas, keliling);
    }

    public Thread createThread() {
        Thread thread = new Thread(this, getNama() + "-Thread");
        thread.setDaemon(true);
        return thread;
    }
}