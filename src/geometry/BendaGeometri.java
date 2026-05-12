 
package geometry;

/**
 * Abstract Class - Demonstrasi Abstract Class
 * Kelas induk abstrak untuk semua benda geometri
 */
public abstract class BendaGeometri {
    
    // Encapsulation - Attribute dengan private modifier
    private String nama;
    private String warna;
    private static int totalObjek = 0;
    
    // Constructor overloading
    public BendaGeometri() {
        this("Benda Geometri", "Putih");
    }
    
    public BendaGeometri(String nama) {
        this(nama, "Putih");
    }
    
    public BendaGeometri(String nama, String warna) {
        this.nama = nama;
        this.warna = warna;
        totalObjek++;
    }
    
    // Encapsulation - Getter dan Setter (Information Hiding)
    public String getNama() {
        return nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public String getWarna() {
        return warna;
    }
    
    public void setWarna(String warna) {
        this.warna = warna;
    }
    
    public static int getTotalObjek() {
        return totalObjek;
    }
    
    // Abstract methods - harus diimplementasikan oleh subclass
    public abstract double hitungLuas();
    public abstract double hitungKeliling();
    public abstract String info();
}

/**
 * Interface untuk benda yang memiliki volume
 * Demonstrasi Interface
 */
interface VolumeCalculable {
    double hitungVolume();
    double hitungLuasPermukaan();
}