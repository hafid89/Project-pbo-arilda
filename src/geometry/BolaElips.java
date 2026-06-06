package geometry;

import exceptions.GeometryException;

/**
 * Kelas Bola Elips (Ellipsoid)
 * Turunan dari Elips sehingga perhitungan elips dapat diturunkan ke BolaElips
 * dan masih dapat dioverride.
 */
public class BolaElips extends Elips implements VolumeCalculable {

    private double sumbuZ;
    protected double volume;
    protected double luasPermukaan;

    private static final double PI = Math.PI;

    public BolaElips() {
        this(1.0, 1.0, 1.0);
    }

    public BolaElips(double sumbuPanjang, double sumbuPendek, double sumbuZ) {
        super(sumbuPanjang, sumbuPendek);
        setNama("Bola Elips (Ellipsoid)");
        setSumbuZ(sumbuZ);
        hitungVolume();
        hitungLuasPermukaan();
    }

    public BolaElips(double semuaSumbu) {
        this(semuaSumbu, semuaSumbu, semuaSumbu);
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

    @Override
    public double hitungVolume() {
        volume = (4.0 / 3.0) * PI * getSumbuPanjang() * getSumbuPendek() * sumbuZ;
        return volume;
    }

    public double hitungVolume(double skala) {
        return hitungVolume() * skala;
    }

    public String hitungVolume(String satuan) {
        return String.format("Volume = %.4f %s", hitungVolume(), satuan);
    }

    @Override
    public double hitungLuasPermukaan() {
        double p = 1.6075;
        double ap = Math.pow(getSumbuPanjang(), p);
        double bp = Math.pow(getSumbuPendek(), p);
        double cp = Math.pow(sumbuZ, p);
        luasPermukaan = 4 * PI * Math.pow(((ap * bp) + (ap * cp) + (bp * cp)) / 3.0, 1.0 / p);
        luas = luasPermukaan;
        return luasPermukaan;
    }

    @Override
    public double hitungLuas() {
        if (sumbuZ <= 0) {
            return super.hitungLuas();
        }
        return hitungLuasPermukaan();
    }

    @Override
    public double hitungKeliling() {
        keliling = super.hitungKeliling();
        return keliling;
    }

    public double getVolume() {
        return volume;
    }

    public double getLuasPermukaan() {
        return luasPermukaan;
    }

    @Override
    public String info() {
        return String.format("""
            === %s ===

            Sumbu Panjang (a): %.4f
            Sumbu Pendek (b): %.4f
            Sumbu Z (c): %.4f

            Volume: %.4f satuan volume
            Luas Permukaan: %.4f satuan luas
            Luas Elips (basis): %.4f satuan luas
            Keliling Elips (basis): %.4f satuan panjang
            """,
            getNama(),
            getSumbuPanjang(),
            getSumbuPendek(),
            sumbuZ,
            volume,
            luasPermukaan,
            super.hitungLuas(),
            super.hitungKeliling()
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

    public static void main(String[] args) {
        System.out.println("=== OBJEK 1 ===");
        BolaElips b1 = new BolaElips(3, 2, 1);
        System.out.println(b1.info());
        System.out.println("Volume dengan skala 2x: " + b1.hitungVolume(2));
        System.out.println(b1.hitungVolume("cm³"));
        System.out.println();
        System.out.println("=== OBJEK 2 ===");
        BolaElips b2 = new BolaElips(5);
        System.out.println(b2.info());
        System.out.println();
        System.out.println("=== SETELAH DIUBAH ===");
        try {
            b1.setSumbuPanjang(6);
            b1.setSumbuPendek(4);
            b1.setSumbuZ(2);
        } catch (GeometryException e) {
            System.err.println("Error memperbarui sumbu: " + e.getMessage());
        }
        System.out.println(b1.info());
    }
}

