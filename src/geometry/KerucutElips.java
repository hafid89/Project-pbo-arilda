package geometry;

import exceptions.GeometryException;

/**
 * Kelas Kerucut dengan alas elips - versi perbaikan (lebih akurat)
 */
public class KerucutElips extends Benda3DimensiAlas {
    
    private double tinggi;
    private double garisPelukis;
    private double volume;
    private double luasPermukaan;
    private static final double PI = Math.PI;
    
    // ================== CONSTRUCTOR ==================
    
    public KerucutElips() {
        this(new Elips(), 1.0);
    }
    
    public KerucutElips(Elips alas, double tinggi) {
        super("Kerucut Alas Elips", "Merah", alas);
        this.tinggi = tinggi;
        updateSemua();
    }
    
    public KerucutElips(double sumbuPanjang, double sumbuPendek, double tinggi) {
        this(new Elips(sumbuPanjang, sumbuPendek), tinggi);
    }
    
    public KerucutElips(double sumbuPanjang, double sumbuPendek, double tinggi, String warna) {
        super("Kerucut Alas Elips", warna, new Elips(sumbuPanjang, sumbuPendek));
        this.tinggi = tinggi;
        updateSemua();
    }
    
    // ================== GETTER ==================
    
    @Override
    public Elips getAlas() {
        return (Elips) super.getAlas();
    }
    
    public double getTinggi() {
        return tinggi;
    }
    
    public double getGarisPelukis() {
        return garisPelukis;
    }
    
    // ================== SETTER ==================
    
    public void setAlas(Elips alas) {
        super.setAlas(alas);
        updateSemua();
    }
    
    public void setTinggi(double tinggi) throws GeometryException {
        if (tinggi <= 0) {
            throw new GeometryException("Tinggi harus > 0", GeometryException.NEGATIVE_VALUE);
        }
        this.tinggi = tinggi;
        updateSemua();
    }
    
    // ================== CORE LOGIC ==================
    
    // Update semua perhitungan
    private void updateSemua() {
        hitungGarisPelukis();
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    // Luas alas elips
    public double hitungLuasAlas() {
        return getAlas().hitungLuas(); // πab
    }
    
    // Garis pelukis (pakai radius efektif √(ab))
    public double hitungGarisPelukis() {
        Elips alas = getAlas();
        double a = alas.getSumbuPanjang();
        double b = alas.getSumbuPendek();
        
        double rEfektif = Math.sqrt(a * b);
        
        garisPelukis = Math.sqrt((tinggi * tinggi) + (rEfektif * rEfektif));
        return garisPelukis;
    }
    
    // Keliling elips (Ramanujan)
    public double hitungKelilingElips() {
        Elips alas = getAlas();
        double a = alas.getSumbuPanjang();
        double b = alas.getSumbuPendek();
        
        return PI * (3 * (a + b) - Math.sqrt((3 * a + b) * (a + 3 * b)));
    }
    
    // Luas selimut (pendekatan numerik)
    public double hitungLuasSelimut() {
        double K = hitungKelilingElips();
        double s = hitungGarisPelukis();
        
        return 0.5 * K * s;
    }
    
    // Volume (BENAR)
    @Override
    public double hitungVolume() {
        volume = (1.0 / 3.0) * hitungLuasAlas() * tinggi;
        return volume;
    }
    
    // Luas permukaan
    @Override
    public double hitungLuasPermukaan() {
        luasPermukaan = hitungLuasAlas() + hitungLuasSelimut();
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
    
    // ================== OUTPUT ==================
    
    @Override
    public String info() {
        Elips alas = getAlas();
        return String.format("""
            === %s ===
            Warna: %s
            Alas Elips: a=%.4f, b=%.4f
            Tinggi: %.4f
            Garis Pelukis: %.4f
            Luas Alas: %.4f satuan luas
            Luas Selimut (aproksimasi): %.4f satuan luas
            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            
            *Catatan: Luas selimut menggunakan pendekatan Ramanujan
            """, 
            getNama(), getWarna(),
            alas.getSumbuPanjang(), alas.getSumbuPendek(),
            tinggi, garisPelukis,
            hitungLuasAlas(), hitungLuasSelimut(),
            volume, luasPermukaan);
    }
}