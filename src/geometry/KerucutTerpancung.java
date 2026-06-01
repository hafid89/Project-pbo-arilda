package geometry;

import exceptions.GeometryException;

/**
 * Kelas Kerucut Terpancung dengan alas elips
 * Inheritance dari KerucutElips - Demonstrasi Inheritance
 */
public class KerucutTerpancung extends KerucutElips {
    
    private double jariJariAtas;
    private Elips alasAtas;
    private boolean isInitialized;  // Flag untuk menandai objek sudah siap
    private static final double PI = Math.PI;
    
    public KerucutTerpancung() {
        this(1.0, 1.0, 1.0, 0.5);
    }
    
    public KerucutTerpancung(Elips alas, double tinggi, double jariJariAtas) {
        super(alas.getSumbuPanjang(), alas.getSumbuPendek(), tinggi);
        this.isInitialized = false;  // Tandai belum siap
        this.jariJariAtas = jariJariAtas;
        this.alasAtas = new Elips(jariJariAtas, jariJariAtas);
        setNama("Kerucut Terpancung Alas Elips");
        this.isInitialized = true;  // Tandai sudah siap
        // Hitung ulang setelah semua properti siap
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    public KerucutTerpancung(double sumbuPanjang, double sumbuPendek, double tinggi, double jariJariAtas) {
        super(sumbuPanjang, sumbuPendek, tinggi);
        this.isInitialized = false;  // Tandai belum siap
        this.jariJariAtas = jariJariAtas;
        this.alasAtas = new Elips(jariJariAtas, jariJariAtas);
        setNama("Kerucut Terpancung Alas Elips");
        this.isInitialized = true;  // Tandai sudah siap
        // Hitung ulang setelah semua properti siap
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    public double getJariJariAtas() {
        return jariJariAtas;
    }
    
    public void setJariJariAtas(double jariJariAtas) throws GeometryException {
        if (jariJariAtas <= 0) {
            throw new GeometryException("Jari-jari atas harus > 0", GeometryException.NEGATIVE_VALUE);
        }
        if (jariJariAtas >= Math.min(getAlas().getSumbuPanjang(), getAlas().getSumbuPendek())) {
            throw new GeometryException("Jari-jari atas harus lebih kecil dari jari-jari alas", 
                                       GeometryException.INVALID_INPUT);
        }
        this.jariJariAtas = jariJariAtas;
        this.alasAtas = new Elips(jariJariAtas, jariJariAtas);
        hitungVolume();
        hitungLuasPermukaan();
    }
    
    @Override
    public double hitungVolume() {
        // Menggunakan rumus volume yang lebih akurat untuk kerucut terpancung dengan alas elips
        double a1 = getSumbuPanjang();
        double b1 = getSumbuPendek();
        double a2 = jariJariAtas;
        double b2 = jariJariAtas;
        
        // Volume frustum dengan penampang elips
        // V = (π × h / 3) × (a1×b1 + a2×b2 + √(a1×b1×a2×b2))
        double luasAlasBawah = a1 * b1;
        double luasAlasAtas = a2 * b2;
        double luasTengah = Math.sqrt(luasAlasBawah * luasAlasAtas);
        
        volume = (PI * getTinggi() / 3.0) * (luasAlasBawah + luasAlasAtas + luasTengah);
        return volume;
    }
    
    @Override
    public double hitungLuasPermukaan() {
        // Cek apakah objek sudah siap digunakan
        if (!isInitialized || alasAtas == null) {
            // Jika belum siap, kembalikan nilai yang sudah ada
            return luasPermukaan;
        }
        
        double luasAlasBawah = PI * getSumbuPanjang() * getSumbuPendek();
        double luasAlasAtas = alasAtas.hitungLuas();
        double luasSelimut = hitungLuasSelimutTerpancung();
        luasPermukaan = luasAlasBawah + luasAlasAtas + luasSelimut;
        return luasPermukaan;
    }
    
    /**
     * Menghitung luas selimut kerucut terpancung alas elips
     *
     * Pendekatan:
     * Luas Selimut ≈ 1/2 × (K_bawah + K_atas) × s
     *
     * dengan:
     * K_bawah = keliling elips bawah (Ramanujan)
     * K_atas  = keliling lingkaran atas
     * s       = garis pelukis frustum
     */
    public double hitungLuasSelimutTerpancung() {

        // Keliling alas bawah (elips)
        double kelilingBawah = getAlas().hitungKeliling();

        // Keliling alas atas (lingkaran)
        double kelilingAtas = 2 * PI * jariJariAtas;

        // Radius efektif bawah
        double radiusBawah =
                Math.sqrt(
                        getSumbuPanjang()
                        * getSumbuPendek()
                );

        // Garis pelukis frustum
        double s =
                Math.sqrt(
                        Math.pow(getTinggi(), 2)
                        + Math.pow(
                                radiusBawah - jariJariAtas,
                                2
                        )
                );

        // Luas selimut frustum
        return 0.5 *
            (kelilingBawah + kelilingAtas)
            * s;
    }    
    public double hitungLuasAlasAtas() {
        if (alasAtas == null) {
            // Fallback jika alasAtas belum diinisialisasi
            return PI * Math.pow(jariJariAtas, 2);
        }
        return alasAtas.hitungLuas();
    }
    
    public double getRadiusEfektifBawah() {
        Elips alasBawah = getAlas();
        return Math.sqrt(alasBawah.getSumbuPanjang() * alasBawah.getSumbuPendek());
    }
    
    public double getRadiusEfektifAtas() {
        return jariJariAtas;
    }
    
    @Override
    public String info() {
        Elips alasBawah = getAlas();
        double radiusEfektifBawah = getRadiusEfektifBawah();
        double radiusEfektifAtas = jariJariAtas;
        
        return String.format("""
            === %s ===
            Alas Bawah: a=%.4f, b=%.4f (radius efektif=%.4f)
            Alas Atas: radius=%.4f (luas=%.4f)
            Tinggi: %.4f
            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            Luas Selimut: %.4f satuan luas
            Rasio (r_atas/r_bawah): %.4f
            """, 
            getNama(),
            alasBawah.getSumbuPanjang(), alasBawah.getSumbuPendek(), radiusEfektifBawah,
            radiusEfektifAtas, hitungLuasAlasAtas(),
            getTinggi(), volume, luasPermukaan, hitungLuasSelimutTerpancung(),
            radiusEfektifAtas / radiusEfektifBawah);
    }

    public Thread createThread() {
        Thread thread = new Thread(this, getNama() + "-Thread");
        thread.setDaemon(true);
        return thread;
    }

    // Method untuk validasi objek
    public boolean isValid() {
        return isInitialized && 
               alasAtas != null && 
               getAlas() != null && 
               getTinggi() > 0 && 
               jariJariAtas > 0 &&
               jariJariAtas < Math.min(getAlas().getSumbuPanjang(), getAlas().getSumbuPendek());
    }
}