package geometry;

import exceptions.GeometryException;

/**
 * Kelas Kerucut dengan alas elips - Benda 3 Dimensi (Limas)
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
        super("Kerucut Alas Elips", "Merah", alas);
        this.tinggi = tinggi;
    }
    
    public KerucutElips(double sumbuPanjang, double sumbuPendek, double tinggi) {
        this(new Elips(sumbuPanjang, sumbuPendek), tinggi);
    }
    
    public KerucutElips(double sumbuPanjang, double sumbuPendek, double tinggi, String warna) {
        super("Kerucut Alas Elips", warna, new Elips(sumbuPanjang, sumbuPendek));
        this.tinggi = tinggi;
    }
    
    @Override
    public Elips getAlas() {
        return (Elips) super.getAlas();
    }
    
    public void setAlas(Elips alas) {
        super.setAlas(alas);
        hitungGarisPelukis();
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    public double getTinggi() {
        return tinggi;
    }
    
    public void setTinggi(double tinggi) throws GeometryException {
        if (tinggi <= 0) {
            throw new GeometryException("Tinggi harus > 0", GeometryException.NEGATIVE_VALUE);
        }
        this.tinggi = tinggi;
        hitungGarisPelukis();
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    public double getGarisPelukis() {
        return garisPelukis;
    }
    
    public double hitungGarisPelukis() {
        Elips alas = getAlas();
        double radiusRata = (alas.getSumbuPanjang() + alas.getSumbuPendek()) / 2;
        garisPelukis = Math.sqrt(Math.pow(tinggi, 2) + Math.pow(radiusRata, 2));
        return garisPelukis;
    }
    
    public double hitungLuasAlas() {
        return getAlas().hitungLuas();
    }
    
    public double hitungLuasSelimut() {
        Elips alas = getAlas();
        double a = alas.getSumbuPanjang();
        double b = alas.getSumbuPendek();
        double s = hitungGarisPelukis();
        return PI * (a + b) * s;
    }
    
    @Override
    public double hitungVolume() {
        volume = (1.0 / 3.0) * hitungLuasAlas() * tinggi;
        return volume;
    }
    
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
            Luas Selimut: %.4f satuan luas
            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            """, 
            getNama(), getWarna(),
            alas.getSumbuPanjang(), alas.getSumbuPendek(),
            tinggi, garisPelukis,
            hitungLuasAlas(), hitungLuasSelimut(),
            volume, luasPermukaan);
    }
}