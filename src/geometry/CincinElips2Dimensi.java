package geometry;

import exceptions.GeometryException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Kelas CincinElips2Dimensi - Turunan dari Elips (Benda 2 Dimensi)
 * Demonstrasi Inheritance dan Overriding
 * Cincin Elips (Elliptical Annulus) adalah daerah antara dua elips konsentris (pusat sama)
 */
public class CincinElips2Dimensi extends Elips {
    
    private double sumbuPanjang2;  // sumbu mayor elips dalam (a2)
    private double sumbuPendek2;   // sumbu minor elips dalam (b2)
    private static final double PI = Math.PI;
    
    // Constructor overloading
    public CincinElips2Dimensi() throws GeometryException {
        this(10.0, 8.0, 6.0, 4.0);
    }
    
    public CincinElips2Dimensi(double sumbuPanjang1, double sumbuPendek1, 
                                double sumbuPanjang2, double sumbuPendek2) throws GeometryException {
        super(sumbuPanjang1, sumbuPendek1);
        setSumbuPanjang2(sumbuPanjang2);
        setSumbuPendek2(sumbuPendek2);
        setNama("Cincin Elips");
    }
    
    // Getter dan Setter untuk Elips Dalam (Encapsulation)
    public double getSumbuPanjang2() {
        return sumbuPanjang2;
    }
    
    public void setSumbuPanjang2(double sumbuPanjang2) throws GeometryException {
        if (sumbuPanjang2 <= 0) {
            throw new GeometryException("Sumbu panjang elips dalam harus > 0", GeometryException.NEGATIVE_VALUE);
        }
        if (sumbuPanjang2 >= getSumbuPanjang()) {
            throw new GeometryException("Sumbu panjang elips dalam harus lebih kecil dari elips luar", 
                                      GeometryException.NEGATIVE_VALUE);
        }
        this.sumbuPanjang2 = sumbuPanjang2;
        hitungLuas();
        hitungKeliling();
    }
    
    public double getSumbuPendek2() {
        return sumbuPendek2;
    }
    
    public void setSumbuPendek2(double sumbuPendek2) throws GeometryException {
        if (sumbuPendek2 <= 0) {
            throw new GeometryException("Sumbu pendek elips dalam harus > 0", GeometryException.NEGATIVE_VALUE);
        }
        if (sumbuPendek2 >= getSumbuPendek()) {
            throw new GeometryException("Sumbu pendek elips dalam harus lebih kecil dari elips luar", 
                                      GeometryException.NEGATIVE_VALUE);
        }
        this.sumbuPendek2 = sumbuPendek2;
        hitungLuas();
        hitungKeliling();
    }
    
    /**
     * Override hitungLuas() - Luas cincin elips
     * Luas Cincin = Luas Elips Luar - Luas Elips Dalam
     */
    @Override
    public double hitungLuas() {
        // Luas elips luar
        double luasElipsLuar = PI * getSumbuPanjang() * getSumbuPendek();
        
        // Luas elips dalam
        double luasElipsDalam = PI * sumbuPanjang2 * sumbuPendek2;
        
        // Luas cincin = selisih luas
        luas = luasElipsLuar - luasElipsDalam;
        return luas;
    }
    
    /**
     * Override hitungKeliling() - Keliling cincin elips
     * Keliling Cincin = Keliling Elips Luar + Keliling Elips Dalam
     */
    @Override
    public double hitungKeliling() {

        // Keliling elips luar
        double kelilingElipsLuar = super.hitungKeliling();

        // Keliling elips dalam
        double kelilingElipsDalam = hitungKelilingElipsDalam();

        // Keliling cincin = jumlah batas luar dan batas dalam
        keliling = kelilingElipsLuar + kelilingElipsDalam;

        return keliling;
    }    
    /**
     * Method tambahan: Menghitung tebal cincin rata-rata
     * Tebal = (a1 - a2) / 2 (menggunakan sumbu panjang)
     */
    public double hitungTebalCincin() {
        double tebalSumbuPanjang = (getSumbuPanjang() - sumbuPanjang2) / 2.0;
        double tebalSumbuPendek = (getSumbuPendek() - sumbuPendek2) / 2.0;
        return (tebalSumbuPanjang + tebalSumbuPendek) / 2.0;
    }
    
    /**
     * Method tambahan: Menghitung luas elips luar
     */
    public double hitungLuasElipsLuar() {
        return PI * getSumbuPanjang() * getSumbuPendek();
    }
    
    /**
     * Method tambahan: Menghitung luas elips dalam
     */
    public double hitungLuasElipsDalam() {
        return PI * sumbuPanjang2 * sumbuPendek2;
    }
    
    /**
     * Method tambahan: Menghitung keliling elips luar
     */
    public double hitungKelilingElipsLuar() {
        return super.hitungKeliling();
    }
    
    /**
     * Method tambahan: Menghitung keliling elips dalam
     * Menggunakan aproksimasi Ramanujan II (lebih akurat)
     */
    public double hitungKelilingElipsDalam() {

        double a2 = sumbuPanjang2;
        double b2 = sumbuPendek2;

        // Jika berbentuk lingkaran
        if (Math.abs(a2 - b2) < 1e-9) {
            return 2 * PI * a2;
        }

        // Ramanujan II
        double h = Math.pow(a2 - b2, 2)
                / Math.pow(a2 + b2, 2);

        return PI * (a2 + b2)
                * (1 + (3 * h)
                / (10 + Math.sqrt(4 - 3 * h)));
    }
    
    /**
     * Method tambahan: Menghitung eksentrisitas elips luar
     */
    public double hitungEksentrisitasLuar() {
        return super.hitungEksentrisitas();
    }
    
    /**
     * Method tambahan: Menghitung eksentrisitas elips dalam
     */
    public double hitungEksentrisitasDalam() {
        if (sumbuPanjang2 <= 0) return 0;
        return Math.sqrt(1 - Math.pow(sumbuPendek2 / sumbuPanjang2, 2));
    }
    
    /**
     * Method tambahan: Menghitung persentase area yang terisi cincin
     */
    public double hitungPersentaseArea() {
        double luasLuar = hitungLuasElipsLuar();
        return (luas / luasLuar) * 100.0;
    }
    
    @Override
    public String info() {
        return String.format("""
            === %s ===
            Elips Luar:
              Sumbu Panjang (a1): %.4f
              Sumbu Pendek (b1): %.4f
              Luas: %.4f satuan luas
              Keliling: %.4f satuan panjang
              Eksentrisitas: %.4f
            
            Elips Dalam:
              Sumbu Panjang (a2): %.4f
              Sumbu Pendek (b2): %.4f
              Luas: %.4f satuan luas
              Keliling: %.4f satuan panjang
              Eksentrisitas: %.4f
            
            Cincin Elips:
              Luas Cincin: %.4f satuan luas
              Keliling Total: %.4f satuan panjang
              Tebal Rata-rata: %.4f satuan panjang
              Persentase Area: %.2f%% dari elips luar
            """, getNama(), 
                getSumbuPanjang(), getSumbuPendek(), hitungLuasElipsLuar(), 
                hitungKelilingElipsLuar(), hitungEksentrisitasLuar(),
                sumbuPanjang2, sumbuPendek2, hitungLuasElipsDalam(),
                hitungKelilingElipsDalam(), hitungEksentrisitasDalam(),
                luas, keliling, hitungTebalCincin(), hitungPersentaseArea());
    }
    
    public String toCSVFormat() {
        return String.format("CincinElips2Dimensi,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f", 
                            getSumbuPanjang(), getSumbuPendek(), 
                            sumbuPanjang2, sumbuPendek2, 
                            luas, keliling, hitungTebalCincin());
    }
    
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(info());
        }
    }
    
    @Override
    public String toString() {
        return String.format("CincinElips2Dimensi(a1=%.2f, b1=%.2f, a2=%.2f, b2=%.2f, luas=%.2f, keliling=%.2f)", 
                            getSumbuPanjang(), getSumbuPendek(), sumbuPanjang2, sumbuPendek2, luas, keliling);
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
