package geometry;

/**
 * Kelas Bola Elips (Ellipsoid)
 * Benda 3 Dimensi
 * Contoh OOP:
 * - Encapsulation
 * - Constructor Overloading
 * - Method Overloading
 * - Overriding
 */
// Catatan OOP:
// - Constructor overloading: beberapa cara membuat objek dengan parameter berbeda
// - Method overloading: `hitungVolume()` dengan parameter berbeda
// - Overriding: implementasi `hitungLuasPermukaan()` khusus ellipsoid
public class BolaElips extends Benda3Dimensi {

    // =========================
    // ENCAPSULATION (private)
    // =========================

    private double sumbuX;
    private double sumbuY;
    private double sumbuZ;

    private static final double PI = Math.PI;

    // =========================
    // CONSTRUCTOR OVERLOADING
    // =========================

    /**
     * Constructor default
     */
    public BolaElips() {
        this(1.0, 1.0, 1.0);
    }

    /**
     * Constructor dengan 3 parameter
     */
    public BolaElips(double sumbuX,
                     double sumbuY,
                     double sumbuZ) {

        super("Bola Elips (Ellipsoid)", "Kuning");

        setSumbuX(sumbuX);
        setSumbuY(sumbuY);
        setSumbuZ(sumbuZ);

        hitungVolume();
        hitungLuasPermukaan();
    }

    /**
     * Constructor overloading
     * Semua sumbu sama
     */
    public BolaElips(double semuaSumbu) {

        super("Bola Elips (Ellipsoid)", "Kuning");

        setSumbuX(semuaSumbu);
        setSumbuY(semuaSumbu);
        setSumbuZ(semuaSumbu);

        hitungVolume();
        hitungLuasPermukaan();
    }

    // =========================
    // GETTER DAN SETTER
    // =========================

    public double getSumbuX() {
        return sumbuX;
    }

    public void setSumbuX(double sumbuX) {

        if (sumbuX > 0) {
            this.sumbuX = sumbuX;
        } else {
            System.out.println("Sumbu X harus > 0");
            this.sumbuX = 1;
        }

        hitungVolume();
        hitungLuasPermukaan();
    }

    public double getSumbuY() {
        return sumbuY;
    }

    public void setSumbuY(double sumbuY) {

        if (sumbuY > 0) {
            this.sumbuY = sumbuY;
        } else {
            System.out.println("Sumbu Y harus > 0");
            this.sumbuY = 1;
        }

        hitungVolume();
        hitungLuasPermukaan();
    }

    public double getSumbuZ() {
        return sumbuZ;
    }

    public void setSumbuZ(double sumbuZ) {

        if (sumbuZ > 0) {
            this.sumbuZ = sumbuZ;
        } else {
            System.out.println("Sumbu Z harus > 0");
            this.sumbuZ = 1;
        }

        hitungVolume();
        hitungLuasPermukaan();
    }

    // =========================
    // OVERRIDING
    // =========================

    /**
     * Override method dari parent
     */
    @Override
    public double hitungVolume() {

        volume =
            (4.0 / 3.0) *
            PI *
            sumbuX *
            sumbuY *
            sumbuZ;

        return volume;
    }

    // =========================
    // METHOD OVERLOADING
    // =========================

    /**
     * Overloading:
     * volume dikali skala
     */
    public double hitungVolume(double skala) {

        return hitungVolume() * skala;
    }

    /**
     * Overloading:
     * menampilkan satuan
     */
    public String hitungVolume(String satuan) {

        return String.format(
            "Volume = %.4f %s",
            hitungVolume(),
            satuan
        );
    }

    /**
     * Override luas permukaan
     */
    @Override
    public double hitungLuasPermukaan() {

        double p = 1.6075;

        double ap = Math.pow(sumbuX, p);
        double bp = Math.pow(sumbuY, p);
        double cp = Math.pow(sumbuZ, p);

        luasPermukaan =
            4 *
            PI *
            Math.pow(
                (
                    (ap * bp)
                    +
                    (ap * cp)
                    +
                    (bp * cp)
                ) / 3.0,
                1.0 / p
            );

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

    // =========================
    // INFORMASI OBJEK
    // =========================

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

            getNama(),
            getWarna(),

            sumbuX,
            sumbuY,
            sumbuZ,

            volume,
            luasPermukaan
        );
    }

    // =========================
    // MAIN TESTING
    // =========================

    public static void main(String[] args) {

        System.out.println("=== OBJEK 1 ===");

        BolaElips b1 =
            new BolaElips(3, 2, 1);

        System.out.println(b1.info());

        // =========================
        // METHOD OVERLOADING
        // =========================

        System.out.println(
            "Volume dengan skala 2x: "
            + b1.hitungVolume(2)
        );

        System.out.println(
            b1.hitungVolume("cm³")
        );

        System.out.println();

        // =========================
        // CONSTRUCTOR OVERLOADING
        // =========================

        System.out.println("=== OBJEK 2 ===");

        BolaElips b2 =
            new BolaElips(5);

        System.out.println(b2.info());

        System.out.println();

        // =========================
        // SETTER TEST
        // =========================

        System.out.println("=== SETELAH DIUBAH ===");

        b1.setSumbuX(6);
        b1.setSumbuY(4);
        b1.setSumbuZ(2);

        System.out.println(b1.info());
    }
}