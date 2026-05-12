package geometry;

/**
 * Kelas Tabung dengan alas elips - Benda 3 Dimensi (Prisma)
 */
public class TabungElips extends Benda3DimensiAlas {
    
    private double tinggi;
    
    public TabungElips() {
        this(new Elips(), 1.0);
    }
    
    public TabungElips(Elips alas, double tinggi) {
        super("Tabung Alas Elips", "Hijau", alas);
        this.tinggi = tinggi;
    }
    
    public TabungElips(double sumbuPanjang, double sumbuPendek, double tinggi) {
        this(new Elips(sumbuPanjang, sumbuPendek), tinggi);
    }
    
    @Override
    public double hitungVolume() {
        volume = getAlas().hitungLuas() * tinggi;
        return volume;
    }
    
    @Override
    public double hitungLuasPermukaan() {
        double luasAlas = getAlas().hitungLuas();
        double kelilingAlas = getAlas().hitungKeliling();
        luasPermukaan = 2 * luasAlas + kelilingAlas * tinggi;
        return luasPermukaan;
    }
    
    @Override
    public double hitungLuas() {
        return hitungLuasPermukaan();
    }
    
    @Override
    public double hitungKeliling() {
        return getAlas().hitungKeliling();
    }
    
    public double getTinggi() {
        return tinggi;
    }
    
    @Override
    public Elips getAlas() {
        return (Elips) super.getAlas();
    }
    
    @Override
    public String info() {
        Elips alas = getAlas();
        return String.format("""
            === %s ===
            Warna: %s
            Alas Elips: a=%.4f, b=%.4f
            Tinggi: %.4f
            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            """, 
            getNama(), getWarna(),
            alas.getSumbuPanjang(), alas.getSumbuPendek(),
            tinggi, volume, luasPermukaan);
    }
}