package geometry;

/**
 * Kelas Tabung dengan alas elips - Benda 3 Dimensi (Prisma)
 */
public class TabungElips extends Elips {
    
    private double tinggi;
    
    public TabungElips() {
        this(1.0, 1.0, 1.0);
    }
    
    public TabungElips(Elips alas, double tinggi) {
        super(alas.sumbuPanjang, alas.sumbuPendek);
        setNama("Tabung Alas Elips");
        this.tinggi = tinggi;
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    public TabungElips(double sumbuPanjang, double sumbuPendek, double tinggi) {
        super(sumbuPanjang, sumbuPendek);
        setNama("Tabung Alas Elips");
        this.tinggi = tinggi;
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    @Override
    public double hitungVolume() {
        volume = super.hitungLuas() * tinggi;
        return volume;
    }
    
    @Override
    public double hitungLuasPermukaan() {
        double luasAlas = super.hitungLuas();
        double kelilingAlas = super.hitungKeliling();
        luasPermukaan = 2 * luasAlas + kelilingAlas * tinggi;
        return luasPermukaan;
    }
    
    @Override
    public double hitungLuas() {
        return hitungLuasPermukaan();
    }
    
    @Override
    public double hitungKeliling() {
        return super.hitungKeliling();
    }
    
    public double getTinggi() {
        return tinggi;
    }
    
    public void setTinggi(double tinggi) {
        this.tinggi = tinggi;
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    public Elips getAlas() {
        return this;
    }
    
    public void updateAll() {
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    @Override
    public String info() {
        Elips alas = getAlas();
        return String.format("""
            === %s ===
            Alas Elips: a=%.4f, b=%.4f
            Tinggi: %.4f
            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            """, 
            getNama(),
            alas.sumbuPanjang, alas.sumbuPendek,
            tinggi, volume, luasPermukaan);
    }

    public Thread createThread() {
        Thread thread = new Thread(this, getNama() + "-Thread");
        thread.setDaemon(true);
        return thread;
    }
}