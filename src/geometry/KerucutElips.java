package geometry;

import exceptions.GeometryException;

/**
 * Kelas Kerucut dengan alas elips - Benda 3 Dimensi (Limas)
 * Menggunakan rumus garis pelukis: s = sqrt(t^2 + (a^2 + b^2)/2)
 */
public class KerucutElips extends Benda3DimensiAlas {
    
    private double tinggi;
    private double garisPelukis;
    private static final double PI = Math.PI;
    
    // Constructor overloading
    public KerucutElips() {
        this(new Elips(), 1.0);
    }
    
    public KerucutElips(Elips alas, double tinggi) {
        super("Kerucut Alas Elips", alas);
        this.tinggi = tinggi;
        updateAll();
    }
    
    public KerucutElips(double sumbuPanjang, double sumbuPendek, double tinggi) {
        this(new Elips(sumbuPanjang, sumbuPendek), tinggi);
    }
    
    // removed obsolete constructor that accepted color
    
    // Method private untuk mengupdate semua perhitungan
    private void updateAll() {
        hitungGarisPelukis();
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    @Override
    public Elips getAlas() {
        return (Elips) super.getAlas();
    }
    
    public void setAlas(Elips alas) {
        super.setAlas(alas);
        updateAll();
    }
    
    public double getTinggi() {
        return tinggi;
    }
    
    public void setTinggi(double tinggi) throws GeometryException {
        if (tinggi <= 0) {
            throw new GeometryException("Tinggi harus > 0", GeometryException.NEGATIVE_VALUE);
        }
        this.tinggi = tinggi;
        updateAll();
    }
    
    public double getGarisPelukis() {
        return garisPelukis;
    }
    
    /**
     * Menghitung garis pelukis dengan rumus:
     * s = sqrt(t^2 + (a^2 + b^2)/2)
     * 
     * Rumus ini merupakan rata-rata kuadrat dari dua garis pelukis sejati:
     * s = sqrt((s1^2 + s2^2)/2) dengan s1 = sqrt(t^2 + a^2), s2 = sqrt(t^2 + b^2)
     */
    public double hitungGarisPelukis() {
        Elips alas = getAlas();
        double a = alas.getSumbuPanjang();
        double b = alas.getSumbuPendek();
        
        // Rumus baru: s = sqrt(t^2 + (a^2 + b^2)/2)
        double rataKuadrat = (a * a + b * b) / 2;
        garisPelukis = Math.sqrt(tinggi * tinggi + rataKuadrat);
        
        return garisPelukis;
    }
    
    public double hitungLuasAlas() {
        return getAlas().hitungLuas();
    }
    
    /**
     * Menghitung luas selimut dengan rumus:
     * L_selimut = pi * (a + b) * s
     * 
     * Catatan: Rumus ini tetap menggunakan pendekatan keliling elips ≈ pi*(a+b)
     * Untuk akurasi lebih tinggi, keliling seharusnya menggunakan pendekatan Ramanujan
     */
    public double hitungLuasSelimut() {
        Elips alas = getAlas();
        double a = alas.getSumbuPanjang();
        double b = alas.getSumbuPendek();
        double s = hitungGarisPelukis();  // menggunakan rumus baru
        return PI * (a + b) * s;
    }
    
    @Override
    public double hitungVolume() {
        // Volume tetap: V = (1/3) * luas_alas * tinggi (eksak)
        volume = (1.0 / 3.0) * hitungLuasAlas() * tinggi;
        return volume;
    }
    
    @Override
    public double hitungLuasPermukaan() {
        // Luas permukaan = luas alas + luas selimut
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
    
    @Override
    public String info() {
        // Pastikan nilai terbaru sebelum menampilkan info
        updateAll();
        
        Elips alas = getAlas();
        return String.format("""
            === %s ===
            Alas Elips: a=%.4f, b=%.4f
            Tinggi: %.4f
            Garis Pelukis (rumus baru): %.4f
            Luas Alas: %.4f satuan luas
            Luas Selimut: %.4f satuan luas
            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            """, 
            getNama(),
            alas.getSumbuPanjang(), alas.getSumbuPendek(),
            tinggi, garisPelukis,
            hitungLuasAlas(), hitungLuasSelimut(),
            volume, luasPermukaan);
    }
}