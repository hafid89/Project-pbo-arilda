package geometry;

/**
 * Kelas Cincin Elips (Elliptic Torus)
 * Benda 3 Dimensi
 * Contoh OOP dengan Encapsulation
 */
public class CincinElips extends BolaElips {

    // =========================
    // ENCAPSULATION (private)
    // =========================

    // Radius utama torus (R)
    private double radiusUtama;

    // Semi-sumbu elips
    private double semiMayor;
    private double semiMinor;

    private static final double PI = Math.PI;

    // =========================
    // Constructor
    // =========================

    /**
     * Constructor default
     */
    public CincinElips() {
        this(5.0, 2.0, 1.0);
    }

    /**
     * Constructor dengan parameter
     *
     * @param radiusUtama Radius utama torus
     * @param semiMayor Semi-sumbu mayor elips
     * @param semiMinor Semi-sumbu minor elips
     */
    public CincinElips(double radiusUtama,
                       double semiMayor,
                       double semiMinor) {

        super();
        setNama("Cincin Elips (Elliptic Torus)");

        // menggunakan setter agar tervalidasi
        setRadiusUtama(radiusUtama);
        setSemiMayor(semiMayor);
        setSemiMinor(semiMinor);

        hitungVolume();
        hitungLuasPermukaan();
    }

    // =========================
    // Getter dan Setter
    // =========================

    /**
     * Getter radius utama
     */
    public double getRadiusUtama() {
        return radiusUtama;
    }

    /**
     * Setter radius utama
     */
    public void setRadiusUtama(double radiusUtama) {

        if (radiusUtama > 0) {
            this.radiusUtama = radiusUtama;
        } else {
            System.out.println("Radius utama harus lebih dari 0");
            this.radiusUtama = 1;
        }

        hitungVolume();
        hitungLuasPermukaan();
    }

    /**
     * Getter semi mayor
     */
    public double getSemiMayor() {
        return semiMayor;
    }

    /**
     * Setter semi mayor
     */
    public void setSemiMayor(double semiMayor) {

        if (semiMayor > 0) {
            this.semiMayor = semiMayor;
        } else {
            System.out.println("Semi mayor harus lebih dari 0");
            this.semiMayor = 1;
        }

        hitungVolume();
        hitungLuasPermukaan();
    }

    /**
     * Getter semi minor
     */
    public double getSemiMinor() {
        return semiMinor;
    }

    /**
     * Setter semi minor
     */
    public void setSemiMinor(double semiMinor) {

        if (semiMinor > 0) {
            this.semiMinor = semiMinor;
        } else {
            System.out.println("Semi minor harus lebih dari 0");
            this.semiMinor = 1;
        }

        hitungVolume();
        hitungLuasPermukaan();
    }

    // =========================
    // Perhitungan Volume
    // =========================

    /**
     * Menghitung volume cincin elips
     *
     * Rumus:
     * V = 2π²Rab
     */
    @Override
    public double hitungVolume() {

        volume =
            2 *
            Math.pow(PI, 2) *
            radiusUtama *
            semiMayor *
            semiMinor;

        return volume;
    }

    // =========================
    // Perhitungan Luas Permukaan
    // =========================

    /**
     * Menghitung luas permukaan cincin elips
     *
     * Pendekatan:
     * L ≈ 4π²R √((a²+b²)/2)
     */
    @Override
    public double hitungLuasPermukaan() {

        double pendekatanElips =
            Math.sqrt(
                (
                    Math.pow(semiMayor, 2)
                    +
                    Math.pow(semiMinor, 2)
                ) / 2.0
            );

        luasPermukaan =
            4 *
            Math.pow(PI, 2) *
            radiusUtama *
            pendekatanElips;

        return luasPermukaan;
    }

    /**
     * Alias luas permukaan
     */
    @Override
    public double hitungLuas() {
        return hitungLuasPermukaan();
    }

    /**
     * Keliling pendekatan
     */
    @Override
    public double hitungKeliling() {

        return
            2 *
            PI *
            (radiusUtama + semiMayor);
    }

    // =========================
    // Informasi Objek
    // =========================

    /**
     * Menampilkan informasi objek
     */
    @Override
    public String info() {

        return String.format("""
            === %s ===

            Radius Utama (R): %.4f
            Semi Mayor Elips (a): %.4f
            Semi Minor Elips (b): %.4f

            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            """,
            getNama(),
            radiusUtama,
            semiMayor,
            semiMinor,
            volume,
            luasPermukaan
        );
    }

    public Thread createThread() {
        Thread thread = new Thread(this, getNama() + "-Thread");
        thread.setDaemon(true);
        return thread;
    }

    // =========================
    // Main Testing
    // =========================

    public static void main(String[] args) {

        CincinElips c1 =
            new CincinElips(5, 2, 1);

        System.out.println(c1.info());

        System.out.println();

        CincinElips c2 =
            new CincinElips(8, 3, 2);

        System.out.println(c2.info());

        System.out.println();

        // Contoh penggunaan setter
        c1.setRadiusUtama(10);
        c1.setSemiMayor(4);
        c1.setSemiMinor(2);

        System.out.println("=== Setelah Diubah ===");
        System.out.println(c1.info());
    }
}