package geometry;

import exceptions.GeometryException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Kelas Tembereng2Dimensi - Turunan dari Juring2Dimensi (Benda 2 Dimensi)
 * Demonstrasi Inheritance dan Overriding
 * Tembereng Elips adalah bagian dari elips yang dibatasi oleh chord (tali busur) dan busurnya
 */
public class Tembereng2Dimensi extends Juring2Dimensi {
    
    private static final double PI = Math.PI;
    
    // Constructor overloading
    public Tembereng2Dimensi() throws GeometryException {
        this(1.0, 1.0, 90.0);
    }
    
    public Tembereng2Dimensi(double sumbuPanjang, double sumbuPendek, double sudutJuring) throws GeometryException {
        super(sumbuPanjang, sumbuPendek, sudutJuring);
        setNama("Tembereng Elips");
        hitungLuas();
        hitungKeliling();
    }
    
    /**
     * Override hitungLuas() - Luas tembereng elips
     * Luas Tembereng = Luas Juring - Luas Segitiga (triangle from center to two endpoints)
     */
    @Override
    public double hitungLuas() {
        // Hitung luas juring terlebih dahulu
        double luasJuring = super.hitungLuas();
        
        // Hitung luas segitiga dengan dua jari-jari setara dan sudut juring
        double luasSegitiga = hitungLuasSegitiga();
        
        // Luas tembereng = luas juring - luas segitiga
        luas = luasJuring - luasSegitiga;
        return luas;
    }
    
    /**
     * Override hitungKeliling() - Keliling tembereng elips
     * Keliling Tembereng = Panjang Chord (tali busur) + Panjang Busur
     */
    @Override
    public double hitungKeliling() {
        // Panjang busur dari juring
        double panjangBusur = hitungPanjangBusur();
        
        // Panjang chord (tali busur)
        double panjangChord = hitungPanjangChord();
        
        // Keliling tembereng = chord + busur
        keliling = panjangChord + panjangBusur;
        return keliling;
    }
    
    /**
     * Method tambahan: Menghitung luas segitiga
     * Segitiga dibentuk oleh pusat elips dan dua titik ujung chord
     * Luas Segitiga = 0.5 × r1 × r2 × sin(sudut)
     * (menggunakan aproksimasi jari-jari rata-rata)
     */
    private double hitungLuasSegitiga() {

        // Konversi sudut juring dari derajat ke radian
        double sudutRadian = Math.toRadians(getSudutJuring());

        // Radius pertama dari pusat elips ke titik awal busur (0°)
        double r1 = hitungJariJariSudut(0);

        // Radius kedua dari pusat elips ke titik akhir busur (θ)
        double r2 = hitungJariJariSudut(sudutRadian);

        // Luas segitiga menggunakan rumus umum:
        // L = 1/2 × sisi1 × sisi2 × sin(sudut)
        // Sisi1 = r1
        // Sisi2 = r2
        return 0.5 * r1 * r2 * Math.sin(sudutRadian);
    }
    
    /**
     * Method tambahan: Menghitung panjang chord (tali busur)
     * Chord menghubungkan dua titik ujung pada elips
     * Menggunakan pendekatan dengan jari-jari pada sudut tertentu
     */
    public double hitungPanjangChord() {

        // Konversi sudut juring dari derajat ke radian
        double sudutRadian = Math.toRadians(getSudutJuring());

        // Radius menuju titik awal busur (0°)
        double r1 = hitungJariJariSudut(0);

        // Radius menuju titik akhir busur (θ)
        double r2 = hitungJariJariSudut(sudutRadian);

        // Menggunakan Hukum Cosinus:
        // chord² = r1² + r2² - 2(r1)(r2)cos(θ)
        return Math.sqrt(
                (r1 * r1) +
                (r2 * r2) -
                (2 * r1 * r2 * Math.cos(sudutRadian))
        );
    } 
    /**
     * Method tambahan: Menghitung tinggi tembereng
     * Tinggi adalah jarak tegak lurus dari chord ke titik terjauh pada busur
     */
    public double hitungTinggiTembereng() {

        // Konversi sudut juring dari derajat ke radian
        double sudutRadian = Math.toRadians(getSudutJuring());

        // Radius elips pada arah tengah juring (θ/2)
        // Digunakan sebagai pendekatan yang lebih baik
        // dibanding memakai rata-rata sumbu elips
        double rTengah =
                hitungJariJariSudut(sudutRadian / 2.0);

        // Tinggi tembereng dihitung dari jarak antara
        // chord dan titik terjauh pada busur
        //
        // Pendekatan:
        // tinggi = r × (1 - cos(θ/2))
        //
        // dengan r menggunakan radius elips pada arah tengah juring
        return rTengah *
                (1 - Math.cos(sudutRadian / 2.0));
    }
    
    /**
     * Method tambahan: Menghitung panjang busur dari tembereng
     */
    @Override
    public double hitungPanjangBusur() {
        return super.hitungPanjangBusur();
    }
    
    /**
     * Method untuk mendapatkan persentase area dibandingkan dengan elips penuh
     */
    public double hitungPersentaseArea() {
        double luasElips = getSumbuPanjang() * getSumbuPendek() * PI;
        return (luas / luasElips) * 100.0;
    }
    
    @Override
    public String info() {
        return String.format("""
            === %s ===
            Sumbu Panjang (a): %.4f
            Sumbu Pendek (b): %.4f
            Sudut Tembereng: %.2f°
            Luas Tembereng: %.4f satuan luas
            Keliling Tembereng: %.4f satuan panjang
            Panjang Busur: %.4f satuan panjang
            Panjang Chord: %.4f satuan panjang
            Tinggi Tembereng: %.4f satuan panjang
            Persentase Area: %.2f%% dari elips penuh
            Eksentrisitas Elips: %.4f
            """, getNama(), getSumbuPanjang(), getSumbuPendek(), getSudutJuring(), 
                luas, keliling, hitungPanjangBusur(), hitungPanjangChord(), 
                hitungTinggiTembereng(), hitungPersentaseArea(), hitungEksentrisitas());
    }
    
    public String toCSVFormat() {
        return String.format("Tembereng2Dimensi,%.4f,%.4f,%.2f,%.4f,%.4f,%.4f", 
                            getSumbuPanjang(), getSumbuPendek(), getSudutJuring(), 
                            luas, keliling, hitungPanjangChord());
    }
    
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(info());
        }
    }
    
    @Override
    public String toString() {
        return String.format("Tembereng2Dimensi(a=%.2f, b=%.2f, sudut=%.2f°, luas=%.2f, keliling=%.2f)", 
                            getSumbuPanjang(), getSumbuPendek(), getSudutJuring(), luas, keliling);
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
