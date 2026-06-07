package geometry;

/**
 * Kelas Tembereng Bola - Benda 3 Dimensi
 * Contoh OOP dengan hubungan pewarisan terhadap Juring
 */
public class Tembereng extends Juring {

    private static final double PI = Math.PI;

    public Tembereng() {
        this(1.0, PI, 0.5);
    }

    public Tembereng(double tinggi, double radiusBola) {
        super(radiusBola, PI, tinggi);
        setNama("Tembereng Bola");
        hitungVolume();
        hitungLuasPermukaan();
    }

    public Tembereng(double jariJari, double sudut, double tinggi) {
        super(jariJari, sudut, tinggi);
        setNama("Tembereng Bola");
        hitungVolume();
        hitungLuasPermukaan();
    }

    public Tembereng(Juring juring) {
        super(juring.getJariJari(), juring.getSudut(), juring.getTinggiPrisma());
        setNama("Tembereng Bola");
        hitungVolume();
        hitungLuasPermukaan();
    }

    public double getTinggi() {
        return tinggi;
    }

    // Setter tinggi menggunakan nilai dari Juring
    public void setTinggi(double tinggi) {
        if (tinggi > 0) {
            this.tinggi = tinggi;
        } else {
            System.out.println("Tinggi harus lebih dari 0");
            this.tinggi = 1;
        }
    }

    // Getter radius bola berdasarkan nilai jari-jari Juring
    public double getRadiusBola() {
        return jariJari;
    }

    // Setter radius bola menggunakan nilai jari-jari Juring
    public void setRadiusBola(double radiusBola) {
        if (radiusBola > 0) {
            this.jariJari = radiusBola;
        } else {
            System.out.println("Radius bola harus lebih dari 0");
            this.jariJari = 1;
        }
    }

    @Override
    public double hitungVolume() {
        double r = jariJari;
        double h = tinggi;
        double fullCapVolume = (PI * Math.pow(h, 2) / 3) * (3 * r - h);
        volume = fullCapVolume * (sudut / (2 * PI));

        return volume;
    }

    public Thread createThread() {
        Thread thread = new Thread(this, getNama() + "-Thread");
        thread.setDaemon(true);
        return thread;
    }

    @Override
    public double hitungLuasPermukaan() {

        // luas sisi lengkung berdasarkan jari-jari Juring
        double luasLengkung = 2 * PI * jariJari * tinggi;

        // luas alas lingkaran
        double luasAlas = PI * Math.pow(jariJari, 2);

        double fullCapSurface = luasLengkung + luasAlas;
        luasPermukaan = fullCapSurface * (sudut / (2 * PI));

        return luasPermukaan;
    }

    @Override
    public double hitungLuas() {
        return hitungLuasPermukaan();
    }

    @Override
    public double hitungKeliling() {
        return 2 * PI * jariJari;
    }

    @Override
    public String info() {

        return String.format("""
                === %s ===
                Tinggi Tembereng: %.4f
                Radius Bola: %.4f
                Sudut Juring: %.4f rad (%.2f°)
                Volume: %.4f satuan volume
                Luas Permukaan: %.4f satuan luas
                """,
                getNama(),
                tinggi,
                jariJari,
                sudut,
                Math.toDegrees(sudut),
                volume,
                luasPermukaan);
    }
}