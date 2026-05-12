package geometry;

import exceptions.GeometryException;

/**
 * Kelas Kerucut Terpancung dengan alas elips
 * Inheritance dari KerucutElips - Demonstrasi Inheritance
 */
public class KerucutTerpancung extends KerucutElips {
    
    private double jariJariAtas;
    private Elips alasAtas;
    private static final double PI = Math.PI;
    
    public KerucutTerpancung() {
        this(new Elips(), 1.0, 0.5);
    }
    
    public KerucutTerpancung(Elips alas, double tinggi, double jariJariAtas) {
        super(alas, tinggi);
        this.jariJariAtas = jariJariAtas;
        this.alasAtas = new Elips(jariJariAtas, jariJariAtas);
        setNama("Kerucut Terpancung Alas Elips");
    }
    
    public KerucutTerpancung(double sumbuPanjang, double sumbuPendek, double tinggi, double jariJariAtas) {
        super(sumbuPanjang, sumbuPendek, tinggi);
        this.jariJariAtas = jariJariAtas;
        this.alasAtas = new Elips(jariJariAtas, jariJariAtas);
        setNama("Kerucut Terpancung Alas Elips");
    }
    
    public double getJariJariAtas() {
        return jariJariAtas;
    }
    
    public void setJariJariAtas(double jariJariAtas) throws GeometryException {
        if (jariJariAtas <= 0) {
            throw new GeometryException("Jari-jari atas harus > 0", GeometryException.NEGATIVE_VALUE);
        }
        this.jariJariAtas = jariJariAtas;
        this.alasAtas = new Elips(jariJariAtas, jariJariAtas);
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    @Override
    public double hitungVolume() {
        double radiusBawah = (getAlas().getSumbuPanjang() + getAlas().getSumbuPendek()) / 2;
        double radiusAtas = jariJariAtas;
        volume = (1.0 / 3.0) * PI * getTinggi() * 
                 (Math.pow(radiusBawah, 2) + radiusBawah * radiusAtas + Math.pow(radiusAtas, 2));
        return volume;
    }
    
    @Override
    public double hitungLuasPermukaan() {
        if (alasAtas == null) {
            throw new IllegalStateException("alasAtas belum diinisialisasi");
        }
        double luasAlasBawah = getAlas().hitungLuas();
        double luasAlasAtas = alasAtas.hitungLuas();
        double luasSelimut = hitungLuasSelimutTerpancung();
        luasPermukaan = luasAlasBawah + luasAlasAtas + luasSelimut;
        return luasPermukaan;
    }
    
    public double hitungLuasSelimutTerpancung() {
        double radiusBawah = (getAlas().getSumbuPanjang() + getAlas().getSumbuPendek()) / 2;
        double radiusAtas = jariJariAtas;
        double s = Math.sqrt(Math.pow(getTinggi(), 2) + Math.pow(radiusBawah - radiusAtas, 2));
        return PI * (radiusBawah + radiusAtas) * s;
    }
    
    public double hitungLuasAlasAtas() {
        if (alasAtas == null) {
            throw new IllegalStateException("alasAtas belum diinisialisasi");
        }
        return alasAtas.hitungLuas();
    }
    
    @Override
    public String info() {
        double radiusBawah = (getAlas().getSumbuPanjang() + getAlas().getSumbuPendek()) / 2;
        return String.format("""
            === %s ===
            Warna: %s
            Alas Bawah: a=%.4f, b=%.4f (R=%.4f)
            Alas Atas: radius=%.4f
            Tinggi: %.4f
            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            """, 
            getNama(), getWarna(),
            getAlas().getSumbuPanjang(), getAlas().getSumbuPendek(), radiusBawah,
            jariJariAtas, getTinggi(), volume, luasPermukaan);
    }
}