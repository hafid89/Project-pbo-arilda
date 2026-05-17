 
package geometry;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Abstract Class - Demonstrasi Abstract Class
 * Kelas induk abstrak untuk semua benda geometri
 */
// OOP notes:
// - Abstraksi: mendefinisikan kontrak umum untuk semua bentuk.
// - Enkapsulasi: atribut `nama` dan `warna` bersifat private, diakses lewat getter/setter.
// - Pewarisan: class ini diwarisi oleh Benda2Dimensi dan Benda3Dimensi.
// - Concurrency: mengimplementasikan `Runnable` sehingga setiap objek dapat dijalankan di thread.
// - Thread pool: menggunakan shared ExecutorService (`newCachedThreadPool()`) untuk menjalankan tugas secara asinkron.
public abstract class BendaGeometri implements Runnable {
    
    // Encapsulation - Attribute dengan private modifier
    private String nama;
    private String warna;
    private static int totalObjek = 0;
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    
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
        incrementTotalObjek();
    }
    
    private static synchronized void incrementTotalObjek() {
        totalObjek++;
    }
    
    // Encapsulation - Getter dan Setter (Information Hiding)
    public synchronized String getNama() {
        return nama;
    }
    
    public synchronized void setNama(String nama) {
        this.nama = nama;
    }
    
    public synchronized String getWarna() {
        return warna;
    }
    
    public synchronized void setWarna(String warna) {
        this.warna = warna;
    }
    
    public static synchronized int getTotalObjek() {
        return totalObjek;
    }
    
    // Abstract methods - harus diimplementasikan oleh subclass
    public abstract double hitungLuas();
    public abstract double hitungKeliling();
    public abstract String info();
    
    @Override
    public final void run() {
        // run() adalah titik masuk saat objek dijalankan di thread.
        // Di sini dilakukan perhitungan yang membutuhkan sinkronisasi pada state objek.
        String namaShape = getNama();
        log(String.format("%s: mulai menghitung...", namaShape));
        try {
            // Delay 2 seconds so the process is clearly visible
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            log(String.format("%s: terinterupsi saat menghitung", namaShape));
            Thread.currentThread().interrupt();
            return;
        }

        // Sinkronisasi mencegah kondisi race saat beberapa thread mengakses state objek.
        synchronized (this) {
            double luas = hitungLuas();
            double keliling = hitungKeliling();
            if (this instanceof VolumeCalculable) {
                VolumeCalculable v = (VolumeCalculable) this;
                double volume = v.hitungVolume();
                double luasPermukaan = v.hitungLuasPermukaan();
                log(String.format("%s: selesai, volume=%.4f, luasPermukaan=%.4f", namaShape, volume, luasPermukaan));
            } else {
                log(String.format("%s: selesai, luas=%.4f, keliling=%.4f", namaShape, luas, keliling));
            }
        }
    }
    
    public void calculateAsync() {
        // Jalankan objek ini secara asinkron pada shared executor
        EXECUTOR.execute(this);
    }
    
    public Future<Double> calculateWithFuture() {
        // Mengembalikan Future sehingga pemanggil dapat menunggu atau membatalkan tugas
        return EXECUTOR.submit(() -> {
            run();
            if (this instanceof VolumeCalculable) {
                return ((VolumeCalculable) this).hitungVolume();
            }
            return hitungLuas();
        });
    }
    
    private static String now() {
        return LocalTime.now().format(TIME_FORMATTER);
    }
    
    private static String threadLabel() {
        return "Thread-" + Thread.currentThread().getId();
    }
    
    protected static void log(String message) {
        System.out.printf("[%s] [%s] %s%n", now(), threadLabel(), message);
    }
}

// VolumeCalculable interface moved to its own file VolumeCalculable.java