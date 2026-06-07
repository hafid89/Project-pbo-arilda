package geometry;

import exceptions.GeometryException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Tembereng Elips
 * Luas Tembereng = Luas Juring - Luas Segitiga
 */
public class Tembereng2Dimensi extends Juring2Dimensi {

    private static final double PI = Math.PI;

    public Tembereng2Dimensi() throws GeometryException {
        this(1.0, 1.0, 90.0);
    }

    public Tembereng2Dimensi(
            double sumbuPanjang,
            double sumbuPendek,
            double sudutJuring) throws GeometryException {

        super(sumbuPanjang, sumbuPendek, sudutJuring);
        setNama("Tembereng Elips");

        hitungLuas();
        hitungKeliling();
    }

    /**
     * Luas Tembereng = Luas Juring - Luas Segitiga
     */
    @Override
    public double hitungLuas() {

        double luasJuring = super.hitungLuas();

        double luasSegitiga = hitungLuasSegitiga();

        luas = luasJuring - luasSegitiga;

        // mencegah hasil negatif akibat error aproksimasi
        if (luas < 0) {
            luas = Math.abs(luas);
        }

        return luas;
    }

    /**
     * Luas segitiga dari dua radius elips sebenarnya
     */
    private double hitungLuasSegitiga() {

        double theta = Math.toRadians(getSudutJuring());

        double r1 = hitungJariJariSudut(-theta / 2.0);
        double r2 = hitungJariJariSudut(theta / 2.0);

        return 0.5 * r1 * r2 * Math.sin(theta);
    }

    /**
     * Keliling tembereng = chord + busur
     */
    @Override
    public double hitungKeliling() {

        keliling = hitungPanjangBusur() + hitungPanjangChord();

        return keliling;
    }

    /**
     * Panjang chord
     */
    public double hitungPanjangChord() {

        double theta = Math.toRadians(getSudutJuring());

        double r1 = hitungJariJariSudut(-theta / 2.0);
        double r2 = hitungJariJariSudut(theta / 2.0);

        return Math.sqrt(
                r1 * r1 +
                r2 * r2 -
                2 * r1 * r2 * Math.cos(theta));
    }

    /**
     * Tinggi tembereng
     */
    public double hitungTinggiTembereng() {

        double theta = Math.toRadians(getSudutJuring());

        double r = hitungJariJariSudut(0);

        return r * (1 - Math.cos(theta / 2.0));
    }

    @Override
    public double hitungPanjangBusur() {
        return super.hitungPanjangBusur();
    }

    /**
     * Persentase area terhadap luas elips penuh
     */
    public double hitungPersentaseArea() {

        double luasElips =
                PI *
                sumbuPanjang *
                sumbuPendek;

        return Math.abs(luas / luasElips) * 100.0;
    }

    @Override
    public String info() {

        return String.format("""
                === %s ===
                Sumbu Panjang (a): %.4f
                Sumbu Pendek (b): %.4f
                Sudut Tembereng: %.2f°
                Luas Tembereng: %.4f satuan luas
                Keliling Tembereng: %.4f satuan panjang
                Panjang Busur: %.4f satuan panjang
                Panjang Chord: %.4f satuan panjang
                Tinggi Tembereng: %.4f satuan panjang
                Persentase Area: %.2f%% dari elips penuh
                Eksentrisitas Elips: %.4f
                """,
                getNama(),
                sumbuPanjang,
                sumbuPendek,
                getSudutJuring(),
                luas,
                keliling,
                hitungPanjangBusur(),
                hitungPanjangChord(),
                hitungTinggiTembereng(),
                hitungPersentaseArea(),
                hitungEksentrisitas());
    }

    public String toCSVFormat() {

        return String.format(
                "Tembereng2Dimensi,%.4f,%.4f,%.2f,%.4f,%.4f,%.4f",
                sumbuPanjang,
                sumbuPendek,
                getSudutJuring(),
                luas,
                keliling,
                hitungPanjangChord());
    }

    public void saveToFile(String filename) throws IOException {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(filename))) {

            writer.write(info());
        }
    }

    @Override
    public String toString() {

        return String.format(
                "Tembereng2Dimensi(a=%.2f, b=%.2f, sudut=%.2f°, luas=%.2f, keliling=%.2f)",
                sumbuPanjang,
                sumbuPendek,
                getSudutJuring(),
                luas,
                keliling);
    }
}