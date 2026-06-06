package geometry;

/**
 * Kelas Prisma Juring
 * (Sector Prism)
 */
public class Juring extends BolaElips {

    private double jariJari;
    private double sudut; // radian
    private double tinggi;

    private static final double PI = Math.PI;

    public Juring() {
        this(3.0, PI / 2, 5.0);
    }

    public Juring(double jariJari,
                  double sudut,
                  double tinggi) {

        super();
        setNama("Prisma Juring");

        this.jariJari = jariJari;
        this.sudut = sudut;
        this.tinggi = tinggi;

        hitungVolume();
        hitungLuasPermukaan();
    }

    /**
     * Volume prisma juring
     */
    @Override
    public double hitungVolume() {

        volume =
            0.5 *
            Math.pow(jariJari, 2) *
            sudut *
            tinggi;

        return volume;
    }

    /**
     * Luas permukaan prisma juring
     */
    @Override
    public double hitungLuasPermukaan() {

        double luasDuaAlas =
            Math.pow(jariJari, 2) * sudut;

        double selimutLengkung =
            jariJari * sudut * tinggi;

        double duaSisiRadial =
            2 * jariJari * tinggi;

        luasPermukaan =
            luasDuaAlas +
            selimutLengkung +
            duaSisiRadial;

        return luasPermukaan;
    }

    @Override
    public double hitungLuas() {
        return hitungLuasPermukaan();
    }

    @Override
    public double hitungKeliling() {

        return
            (jariJari * sudut)
            + 2 * jariJari;
    }

    @Override
    public String info() {

        return String.format("""
            === %s ===
            Jari-jari: %.4f
            Sudut: %.4f rad (%.2f°)
            Tinggi: %.4f

            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            """,

            getNama(),

            jariJari,
            sudut,
            Math.toDegrees(sudut),

            tinggi,

            volume,
            luasPermukaan
        );
    }

    public Thread createThread() {
        Thread thread = new Thread(this, getNama() + "-Thread");
        thread.setDaemon(true);
        return thread;
    }

    public void startWorkerAndMaybeInterrupt(BendaGeometri other) {
        startWorker();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (other != null) interruptOther(other);
    }
}