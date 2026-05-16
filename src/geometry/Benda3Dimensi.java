package geometry;

/**
 * Abstract class untuk benda 3 dimensi
 * Implementasi interface VolumeCalculable
 */
public abstract class Benda3Dimensi extends BendaGeometri implements VolumeCalculable {
    
    protected double volume;
    protected double luasPermukaan;
    
    public Benda3Dimensi(String nama, String warna) {
        super(nama, warna);
    }
    
    public Benda3Dimensi(String nama) {
        super(nama);
    }
    
    @Override
    public abstract double hitungVolume();
    
    @Override
    public abstract double hitungLuasPermukaan();
    
    @Override
    public abstract double hitungLuas();
    
    @Override
    public abstract double hitungKeliling();
    
    public synchronized double getVolume() {
        return volume;
    }
    
    public synchronized double getLuasPermukaan() {
        return luasPermukaan;
    }
    
    @Override
    public String info() {
        return String.format("""
            === %s ===
            Warna: %s
            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            """, getNama(), getWarna(), volume, luasPermukaan);
    }
}