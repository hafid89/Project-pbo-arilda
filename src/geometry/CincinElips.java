package geometry;

/**
 * Kelas Cincin dengan pola dasar elips (Torus)
 * Benda 3 Dimensi (Prisma)
 */
public class CincinElips extends Benda3Dimensi {
    
    private double jariJariLuar;
    private double jariJariDalam;
    private double tinggi;
    private static final double PI = Math.PI;
    
    public CincinElips() {
        this(2.0, 0.5, 1.0);
    }
    
    public CincinElips(double jariJariLuar, double jariJariDalam, double tinggi) {
        super("Cincin Elips (Torus)", "Pink");
        this.jariJariLuar = jariJariLuar;
        this.jariJariDalam = jariJariDalam;
        this.tinggi = tinggi;
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    @Override
    public double hitungVolume() {
        volume = 2 * Math.pow(PI, 2) * jariJariLuar * Math.pow(jariJariDalam, 2);
        return volume;
    }
    
    @Override
    public double hitungLuasPermukaan() {
        luasPermukaan = 4 * Math.pow(PI, 2) * jariJariLuar * jariJariDalam;
        return luasPermukaan;
    }
    
    @Override
    public double hitungLuas() {
        return hitungLuasPermukaan();
    }
    
    @Override
    public double hitungKeliling() {
        return 2 * PI * (jariJariLuar + jariJariDalam);
    }
    
    @Override
    public String info() {
        return String.format("""
            === %s ===
            Warna: %s
            Jari-jari Luar (R): %.4f
            Jari-jari Dalam (r): %.4f
            Tinggi: %.4f
            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            """, 
            getNama(), getWarna(), jariJariLuar, jariJariDalam, tinggi, volume, luasPermukaan);
    }
} 
