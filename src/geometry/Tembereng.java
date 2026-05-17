package geometry;

/**
 * Kelas Tembereng Bola - Benda 3 Dimensi
 * Contoh OOP dengan Encapsulation
 */
public class Tembereng extends Benda3Dimensi {

    private double tinggi;
    private double radiusBola;

    private static final double PI = Math.PI;

    public Tembereng() {
        this(0.5, 1.0);
    }

    public Tembereng(double tinggi, double radiusBola) {
        super("Tembereng Bola");

        // menggunakan setter agar tervalidasi
        setTinggi(tinggi);
        setRadiusBola(radiusBola);

        hitungVolume();
        hitungLuasPermukaan();
    }

    public double getTinggi() {
        return tinggi;
    }

    // Setter tinggi
    public void setTinggi(double tinggi) {
        if (tinggi > 0) {
            this.tinggi = tinggi;
        } else {
            System.out.println("Tinggi harus lebih dari 0");
            this.tinggi = 1;
        }
    }

    // Getter radius bola
    public double getRadiusBola() {
        return radiusBola;
    }

    // Setter radius bola
    public void setRadiusBola(double radiusBola) {
        if (radiusBola > 0) {
            this.radiusBola = radiusBola;
        } else {
            System.out.println("Radius bola harus lebih dari 0");
            this.radiusBola = 1;
        }
    }

    @Override
    public double hitungVolume() {
        volume = (PI * Math.pow(tinggi, 2) / 3)
                * (3 * radiusBola - tinggi);

        return volume;
    }

    @Override
    public double hitungLuasPermukaan() {

        // luas sisi lengkung
        double luasLengkung = 2 * PI * radiusBola * tinggi;

        // luas alas lingkaran
        double luasAlas = PI * Math.pow(radiusBola, 2);

        luasPermukaan = luasLengkung + luasAlas;

        return luasPermukaan;
    }

    @Override
    public double hitungLuas() {
        return hitungLuasPermukaan();
    }

    @Override
    public double hitungKeliling() {
        return 2 * PI * radiusBola;
    }

    @Override
    public String info() {

        return String.format("""
                === %s ===
                Tinggi Tembereng: %.4f
                Radius Bola: %.4f
                Volume: %.4f satuan volume
                Luas Permukaan: %.4f satuan luas
                """,
                getNama(),
                tinggi,
                radiusBola,
                volume,
                luasPermukaan);
    }
}