package geometry;

/**
 * Kelas Tembereng Bola - Benda 3 Dimensi (Prisma)
 */
public class Tembereng extends Benda3Dimensi {
    
    private double tinggi;
    private double radiusBola;
    private static final double PI = Math.PI;
    
    public Tembereng() {
        this(0.5, 1.0);
    }
    
    public Tembereng(double tinggi, double radiusBola) {
        super("Tembereng Bola", "Ungu");
        this.tinggi = tinggi;
        this.radiusBola = radiusBola;
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    @Override
    public double hitungVolume() {
        volume = (PI * Math.pow(tinggi, 2) / 3) * (3 * radiusBola - tinggi);
        return volume;
    }
    
    @Override
    public double hitungLuasPermukaan() {
        double luasLengkung = 2 * PI * radiusBola * tinggi;
        double luasAlas = PI * Math.pow(radiusBola, 2);
        luasPermukaan = luasLengkung + luasAlas;
        return luasPermukaan;
    }
    
    @Override
    public double hitungLuas() {
        return hitungLuasPermukaan();
    }
    
    @Override
    public double hitungKeliling() {
        return 2 * PI * radiusBola;
    }
    
    @Override
    public String info() {
        return String.format("""
            === %s ===
            Warna: %s
            Jari-jari Alas: %.4f
            Tinggi Tembereng: %.4f
            Radius Bola: %.4f
            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            """, 
            getNama(), getWarna(), tinggi, radiusBola, volume, luasPermukaan);
    }
} 
