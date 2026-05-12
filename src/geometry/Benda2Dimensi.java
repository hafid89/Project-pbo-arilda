package geometry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Abstract class untuk benda 2 dimensi
 * Inheritance dari BendaGeometri
 */
public abstract class Benda2Dimensi extends BendaGeometri {
    
    protected double luas;
    protected double keliling;
    
    public Benda2Dimensi(String nama, String warna) {
        super(nama, warna);
    }
    
    public Benda2Dimensi(String nama) {
        super(nama);
    }
    
    @Override
    public abstract double hitungLuas();
    
    @Override
    public abstract double hitungKeliling();
    
    public double getLuas() {
        return luas;
    }
    
    public double getKeliling() {
        return keliling;
    }
    
    public void saveInfoToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(info());
        }
    }
    
    public String toCSVFormat() {
        return String.format("%s,%s,%.4f,%.4f", getNama(), getWarna(), luas, keliling);
    }
    
    @Override
    public String info() {
        return String.format("""
            === %s ===
            Warna: %s
            Luas: %.4f satuan luas
            Keliling: %.4f satuan panjang
            """, getNama(), getWarna(), luas, keliling);
    }
}