package geometry;

import exceptions.GeometryException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Kelas JuringElips - Turunan dari Elips (Benda 2 Dimensi)
 * Demonstrasi Inheritance dan Overriding
 * Juring Elips adalah bagian dari elips yang dibatasi oleh dua jari-jari dari pusat dan busurnya
 */
public class Juring2Dimensi extends Elips {
    
    private double sudutJuring;  // sudut dalam derajat (0-360)
    private static final double PI = Math.PI;
    
    // Constructor overloading
    public Juring2Dimensi() throws GeometryException {
        this(1.0, 1.0, 90.0);
    }
    
    public Juring2Dimensi(double sumbuPanjang, double sumbuPendek, double sudutJuring) throws GeometryException {
        super(sumbuPanjang, sumbuPendek);
        setSudutJuring(sudutJuring);
        setNama("Juring Elips");
    }
    
    // Getter dan Setter untuk Sudut Juring (Encapsulation)
    public double getSudutJuring() {
        return sudutJuring;
    }
    
    public void setSudutJuring(double sudutJuring) throws GeometryException {
        if (sudutJuring <= 0 || sudutJuring > 360) {
            throw new GeometryException("Sudut juring harus antara 0 dan 360 derajat", GeometryException.NEGATIVE_VALUE);
        }
        this.sudutJuring = sudutJuring;
        hitungLuas();
        hitungKeliling();
    }
    
    /**
     * Override hitungLuas() - Luas juring elips
     * Luas Juring = (sudut / 360) × π × a × b
     */
    @Override
    public double hitungLuas() {
        double luasElips = super.hitungLuas();
        luas = (sudutJuring / 360.0) * luasElips;
        return luas;
    }
    
    /**
     * Override hitungKeliling() - Keliling juring elips.
     * Keliling Juring = dua jari-jari setara + panjang busur.
     */
    @Override
    public double hitungKeliling() {
        // Panjang busur elips (aproksimasi menggunakan proporsi keliling)
        double kelilingElips = super.hitungKeliling();
        double panjangBusur = (sudutJuring / 360.0) * kelilingElips;

        // Radius pertama (sudut 0°)
        double r1 = hitungJariJariSudut(0);

        // Radius kedua (sudut akhir juring)
        double r2 = hitungJariJariSudut(Math.toRadians(sudutJuring));

        // Keliling juring = radius pertama + radius kedua + panjang busur
        keliling = r1 + r2 + panjangBusur;

        return keliling;
    }
    
    /**
     * Method tambahan: Menghitung panjang busur juring elips
     */
    public double hitungPanjangBusur() {
        double kelilingElips = super.hitungKeliling();
        return (sudutJuring / 360.0) * kelilingElips;
    }
    
    /**
     * Method tambahan: Menghitung panjang jari-jari dari pusat ke keliling elips
     * Ini menggunakan aproksimasi untuk elips
     */
    public double hitungJariJariSudut(double sudutRadian) {
        double a = getSumbuPanjang();
        double b = getSumbuPendek();
        double cosSudut = Math.cos(sudutRadian);
        double sinSudut = Math.sin(sudutRadian);
        
        // Formula untuk jari-jari elips pada sudut tertentu
        double penyebut = Math.sqrt(Math.pow(b * cosSudut, 2) + Math.pow(a * sinSudut, 2));
        return (a * b) / penyebut;
    }
    
    @Override
    public String info() {
        return String.format("""
            === %s ===
            Sumbu Panjang (a): %.4f
            Sumbu Pendek (b): %.4f
            Sudut Juring: %.2f°
            Luas Juring: %.4f satuan luas
            Keliling Juring: %.4f satuan panjang
            Panjang Busur: %.4f satuan panjang
            Eksentrisitas Elips: %.4f
            """, getNama(), getSumbuPanjang(), getSumbuPendek(), sudutJuring, luas, keliling, 
                hitungPanjangBusur(), hitungEksentrisitas());
    }
    
    public String toCSVFormat() {
        return String.format("Juring2Dimensi,%.4f,%.4f,%.2f,%.4f,%.4f", 
                            getSumbuPanjang(), getSumbuPendek(), sudutJuring, luas, keliling);
    }
    
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(info());
        }
    }
    
    @Override
    public String toString() {
        return String.format("Juring2Dimensi(a=%.2f, b=%.2f, sudut=%.2f°, luas=%.2f, keliling=%.2f)", 
                            getSumbuPanjang(), getSumbuPendek(), sudutJuring, luas, keliling);
    }

    public void startWorkerAndMaybeInterrupt(BendaGeometri other) {
        startWorker();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (other != null) interruptOther(other);
    }
}
