package geometry;

/**
 * Kelas Bola Elips (Ellipsoid) - Benda 3 Dimensi (Prisma)
 */
public class BolaElips extends Benda3Dimensi {
    
    private double sumbuX;
    private double sumbuY;
    private double sumbuZ;
    private static final double PI = Math.PI;
    
    public BolaElips() {
        this(1.0, 1.0, 1.0);
    }
    
    public BolaElips(double sumbuX, double sumbuY, double sumbuZ) {
        super("Bola Elips (Ellipsoid)", "Kuning");
        this.sumbuX = sumbuX;
        this.sumbuY = sumbuY;
        this.sumbuZ = sumbuZ;
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    @Override
    public double hitungVolume() {
        volume = (4.0 / 3.0) * PI * sumbuX * sumbuY * sumbuZ;
        return volume;
    }
    
    @Override
    public double hitungLuasPermukaan() {
        // Pendekatan rumus luas permukaan ellipsoid
        double p = 1.6075;
        double ap = Math.pow(sumbuX, p);
        double bp = Math.pow(sumbuY, p);
        double cp = Math.pow(sumbuZ, p);
        luasPermukaan = 4 * PI * Math.pow((ap * bp + ap * cp + bp * cp) / 3, 1 / p);
        return luasPermukaan;
    }
    
    @Override
    public double hitungLuas() {
        return hitungLuasPermukaan();
    }
    
    @Override
    public double hitungKeliling() {
        return 0;
    }
    
    @Override
    public String info() {
        return String.format("""
            === %s ===
            Warna: %s
            Radius X (a): %.4f
            Radius Y (b): %.4f
            Radius Z (c): %.4f
            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            """, 
            getNama(), getWarna(), sumbuX, sumbuY, sumbuZ, volume, luasPermukaan);
    }
}