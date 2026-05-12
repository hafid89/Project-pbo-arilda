package geometry;

/**
 * Kelas Juring Bola - Benda 3 Dimensi (Prisma)
 */
public class Juring extends Benda3Dimensi {
    
    private double jariJari;
    private double sudut;
    private double tinggi;
    private static final double PI = Math.PI;
    
    public Juring() {
        this(1.0, PI / 2, 1.0);
    }
    
    public Juring(double jariJari, double sudut, double tinggi) {
        super("Juring Bola", "Orange");
        this.jariJari = jariJari;
        this.sudut = sudut;
        this.tinggi = tinggi;
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    @Override
    public double hitungVolume() {
        volume = (1.0 / 3.0) * Math.pow(jariJari, 3) * sudut;
        return volume;
    }
    
    @Override
    public double hitungLuasPermukaan() {
        double luasSelimut = 2 * PI * jariJari * tinggi;
        double luasAlas = PI * Math.pow(jariJari, 2) * (sudut / (2 * PI));
        luasPermukaan = luasSelimut + luasAlas;
        return luasPermukaan;
    }
    
    public double hitungLuasAlas() {
        return PI * Math.pow(jariJari, 2) * (sudut / (2 * PI));
    }
    
    @Override
    public double hitungLuas() {
        return hitungLuasPermukaan();
    }
    
    @Override
    public double hitungKeliling() {
        return 2 * PI * jariJari;
    }
    
    @Override
    public String info() {
        return String.format("""
            === %s ===
            Warna: %s
            Jari-jari: %.4f
            Sudut: %.4f rad (%.2f°)
            Tinggi: %.4f
            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            """, 
            getNama(), getWarna(), jariJari, sudut, Math.toDegrees(sudut), 
            tinggi, volume, luasPermukaan);
    }
}