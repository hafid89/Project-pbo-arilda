package geometry;

/**
 * Kelas Cincin Elips (Elliptic Torus)
 * Benda 3 Dimensi
 */
public class CincinElips extends Benda3Dimensi {

    // Radius utama torus
    private double radiusUtama;

    // Semi-sumbu elips
    private double semiMayor;
    private double semiMinor;

    private static final double PI = Math.PI;

    /**
     * Constructor default
     */
    public CincinElips() {
        this(5.0, 2.0, 1.0);
    }

    /**
     * Constructor dengan parameter
     *
     * @param radiusUtama Radius utama torus (R)
     * @param semiMayor   Semi-sumbu mayor elips (a)
     * @param semiMinor   Semi-sumbu minor elips (b)
     */
    public CincinElips(double radiusUtama,
                       double semiMayor,
                       double semiMinor) {

        super("Cincin Elips (Elliptic Torus)", "Pink");

        this.radiusUtama = radiusUtama;
        this.semiMayor = semiMayor;
        this.semiMinor = semiMinor;

        hitungVolume();
        hitungLuasPermukaan();
    }

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
                (Math.pow(semiMayor, 2)
                + Math.pow(semiMinor, 2)) / 2.0
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

        return 2 * PI * (radiusUtama + semiMayor);
    }

    /**
     * Informasi objek
     */
    @Override
    public String info() {

        return String.format("""
            === %s ===
            Warna: %s

            Radius Utama (R): %.4f
            Semi Mayor Elips (a): %.4f
            Semi Minor Elips (b): %.4f

            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            """,

            getNama(),
            getWarna(),

            radiusUtama,
            semiMayor,
            semiMinor,

            volume,
            luasPermukaan
        );
    }

    /**
     * Getter dan Setter
     */
    public double getRadiusUtama() {
        return radiusUtama;
    }

    public void setRadiusUtama(double radiusUtama) {
        this.radiusUtama = radiusUtama;
        hitungVolume();
        hitungLuasPermukaan();
    }

    public double getSemiMayor() {
        return semiMayor;
    }

    public void setSemiMayor(double semiMayor) {
        this.semiMayor = semiMayor;
        hitungVolume();
        hitungLuasPermukaan();
    }

    public double getSemiMinor() {
        return semiMinor;
    }

    public void setSemiMinor(double semiMinor) {
        this.semiMinor = semiMinor;
        hitungVolume();
        hitungLuasPermukaan();
    }

    /**
     * Main untuk testing
     */
    public static void main(String[] args) {

        CincinElips c1 =
            new CincinElips(5, 2, 1);

        System.out.println(c1.info());

        CincinElips c2 =
            new CincinElips(8, 3, 2);

        System.out.println(c2.info());
    }
}