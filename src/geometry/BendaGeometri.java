 
package geometry;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Abstract Class - Demonstrasi Abstract Class
 * Kelas induk abstrak untuk semua benda geometri
 */
public abstract class BendaGeometri implements Runnable {
    
    /**
     * Lima pilar OOP yang diaplikasikan di sini:
     * 1) Abstraction: `BendaGeometri` sebagai abstraksi umum untuk semua bentuk.
     * 2) Encapsulation: akses data melalui getter/setter; field yang
     *    perlu diakses turunan dibuat `protected`.
     * 3) Inheritance: kelas turunan seperti `Elips`, `TabungElips`.
     * 4) Polymorphism: pemanggilan `hitungLuas()`/`hitungVolume()` lewat tipe induk.
     * 5) Concurrency: setiap kelas dapat membuat thread, terdaftar, dan saling
     *    menginterupsi untuk memperlihatkan proses multithreading.
     */

    // Encapsulation - untuk kelas abstrak anggota yang digunakan turunan dibuat protected
    protected String nama;  // nama benda (turunan boleh akses langsung jika perlu)
    private static int totalObjek = 0;
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    // Registry untuk thread aktif (memungkinkan interupsi silang)
    private static final List<Thread> ACTIVE_THREADS = Collections.synchronizedList(new ArrayList<>());
    protected Thread worker; // worker thread untuk instance ini

    protected int totalIterations = 1;
    protected int iterationDelayMs = 1000;
    protected int currentIteration = 0;
    protected DemoProgressListener progressListener;

    public interface DemoProgressListener {
        void onProgress(BendaGeometri shape, int currentIteration, int totalIterations, String message);
    }

    public int getCurrentIteration() {
        return currentIteration;
    }

    public int getTotalIterations() {
        return totalIterations;
    }

    public boolean isComplete() {
        return currentIteration >= totalIterations;
    }
    
    // Constructor overloading
    public BendaGeometri() {
        this("Benda Geometri");
    }
    
    public BendaGeometri(String nama) {
        this.nama = nama;
        incrementTotalObjek();
    }
    
    private static synchronized void incrementTotalObjek() {
        totalObjek++;
    }
    
    // Encapsulation - Getter dan Setter (Information Hiding)
    public synchronized String getNama() { // getter itu ngambil
        return nama;                       // setter itu ngubah
    }
    
    public synchronized void setNama(String nama) { // set pasti ada this
        this.nama = nama;                           // get ada return
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
        String namaShape = getNama();
        registerThread(Thread.currentThread());

        int startItem = Math.max(currentIteration + 1, 1);
        if (startItem > totalIterations) {
            log(String.format("%s: sudah selesai semua %d item", namaShape, totalIterations));
            return;
        }

        log(String.format("%s: mulai menghitung item %d hingga %d dari total %d item", namaShape, startItem, totalIterations, totalIterations));

        for (int item = startItem; item <= totalIterations; item++) {
            if (Thread.currentThread().isInterrupted()) {
                log(String.format("%s: dihentikan sebelum menyelesaikan item %d/%d", namaShape, item, totalIterations));
                break;
            }

            log(String.format("%s: memproses item %d/%d", namaShape, item, totalIterations));
            if (progressListener != null) {
                progressListener.onProgress(this, item, totalIterations,
                        String.format("Memproses item %d dari %d", item, totalIterations));
            }

            try {
                Thread.sleep(iterationDelayMs);
            } catch (InterruptedException ie) {
                log(String.format("%s: terinterupsi saat memproses item %d/%d", namaShape, item, totalIterations));
                Thread.currentThread().interrupt();
                break;
            }

            synchronized (this) {
                double luas = hitungLuas();
                double keliling = hitungKeliling();
                if (this instanceof VolumeCalculable) {
                    VolumeCalculable v = (VolumeCalculable) this;
                    double volume = v.hitungVolume();
                    double luasPermukaan = v.hitungLuasPermukaan();
                    log(String.format("%s: item %d selesai, volume=%.4f, luasPermukaan=%.4f", namaShape, item, volume, luasPermukaan));
                } else {
                    log(String.format("%s: item %d selesai, luas=%.4f, keliling=%.4f", namaShape, item, luas, keliling));
                }
            }

            currentIteration = item;
        }

        if (currentIteration >= totalIterations) {
            log(String.format("%s: selesai semua %d item", namaShape, totalIterations));
        } else {
            log(String.format("%s: dihentikan pada item %d/%d", namaShape, currentIteration, totalIterations));
        }
    }
    
    public void calculateAsync() {
        EXECUTOR.execute(this);
    }
    
    public Future<Double> calculateWithFuture() {
        return EXECUTOR.submit(() -> {
            run();
            if (this instanceof VolumeCalculable) {
                return ((VolumeCalculable) this).hitungVolume();
            }
            return hitungLuas();
        });
    }

    public void setTotalIterations(int totalIterations) {
        if (totalIterations > 0) {
            this.totalIterations = totalIterations;
        }
    }

    public void setIterationDelayMs(int iterationDelayMs) {
        if (iterationDelayMs > 0) {
            this.iterationDelayMs = iterationDelayMs;
        }
    }

    public void setProgressListener(DemoProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public Thread createThread() {
        Thread thread = new Thread(this, getNama() + "-Thread");
        thread.setDaemon(true);
        return thread;
    }

    // Register thread ke daftar aktif (dipanggil saat thread dibuat/dimulai)
    public void registerThread(Thread t) {
        if (t != null) {
            ACTIVE_THREADS.add(t);
        }
    }

    // Mulai worker thread untuk instance ini dan daftarkan
    public void startWorker() {
        worker = createThread();
        registerThread(worker);
        worker.start();
    }

    public Thread getWorker() {
        return worker;
    }

    // Interrupt worker milik instance ini
    public void interruptWorker() {
        if (worker != null) {
            worker.interrupt();
        }
    }

    // Meminta interupsi pada instance lain
    public void interruptOther(BendaGeometri other) {
        if (other != null) {
            log(String.format("%s meminta interupsi -> %s", getNama(), other.getNama()));
            other.interruptWorker();
        }
    }

    public static void main(String[] args) {
        runMultithreadingDemo();
    }

    public static void runMultithreadingDemo() {
        List<List<BendaGeometri>> cycleOne = new ArrayList<>();
        cycleOne.add(List.of(new Elips(4.0, 2.0), new Elips(6.0, 3.0)));
        cycleOne.add(List.of(new TabungElips(4.0, 2.0, 5.0), new TabungElips(3.0, 1.5, 4.0)));
        cycleOne.add(List.of(new KerucutElips(4.0, 2.0, 5.0), new KerucutElips(3.0, 3.0, 4.0)));
        cycleOne.add(List.of(new BolaElips(3.0, 2.0, 1.5), new BolaElips(4.0, 3.0, 2.0)));
        cycleOne.add(List.of(new KerucutTerpancung(4.0, 2.0, 5.0, 1.0), new KerucutTerpancung(3.0, 3.0, 4.0, 0.5)));
        cycleOne.add(List.of(new Juring(2.5, Math.PI / 2.0, 1.0), new Juring(3.0, Math.PI / 3.0, 1.2)));
        cycleOne.add(List.of(new Tembereng(1.0, 2.0), new Tembereng(1.5, 2.5)));
        cycleOne.add(List.of(new CincinElips(3.5, 1.2, 1.0), new CincinElips(4.0, 1.5, 1.2)));

        List<List<BendaGeometri>> cycleTwo = new ArrayList<>();
        cycleTwo.add(List.of(new Elips(5.0, 1.5), new Elips(7.0, 2.5)));
        cycleTwo.add(List.of(new TabungElips(3.0, 2.0, 4.0), new TabungElips(5.0, 2.5, 6.0)));
        cycleTwo.add(List.of(new KerucutElips(5.0, 2.0, 4.0), new KerucutElips(2.0, 1.0, 3.0)));
        cycleTwo.add(List.of(new BolaElips(2.0, 2.0, 3.0), new BolaElips(3.0, 2.0, 4.0)));
        cycleTwo.add(List.of(new KerucutTerpancung(5.0, 2.0, 4.0, 0.8), new KerucutTerpancung(4.0, 1.5, 3.5, 0.9)));
        cycleTwo.add(List.of(new Juring(2.0, Math.PI / 3.0, 2.0), new Juring(4.0, Math.PI / 4.0, 2.5)));
        cycleTwo.add(List.of(new Tembereng(0.8, 1.5), new Tembereng(1.2, 2.0)));
        cycleTwo.add(List.of(new CincinElips(4.5, 1.4, 1.1), new CincinElips(5.0, 1.6, 1.3)));

        List<List<List<BendaGeometri>>> allCycles = List.of(cycleOne, cycleTwo);

        for (int cycle = 0; cycle < allCycles.size(); cycle++) {
            System.out.printf("=== CYCLE %d ===%n", cycle + 1);
            for (int groupIndex = 0; groupIndex < allCycles.get(cycle).size(); groupIndex++) {
                List<BendaGeometri> group = allCycles.get(cycle).get(groupIndex);
                System.out.printf("--- Start group %d: %s --- %n", groupIndex + 1,
                        group.get(0).getClass().getSimpleName());

                List<Thread> activeThreads = new ArrayList<>();
                for (BendaGeometri shape : group) {
                    shape.startWorker();
                    activeThreads.add(shape.worker);
                }

                if (group.size() >= 2) {
                    BendaGeometri primary = group.get(0);
                    BendaGeometri secondary = group.get(1);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    secondary.interruptOther(primary);
                }

                for (Thread thread : activeThreads) {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                System.out.printf("--- Group %d selesai ---\n", groupIndex + 1);
            }
        }

        System.out.println("=== SEMUA OBJEK SELESAI ===\n");
        runLargeScaleComparison();
    }

    private static void runLargeScaleComparison() {
        final int count = 100;
        List<Elips> elipses = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            double a = 2.0 + (i % 5) * 0.5;
            double b = 1.0 + (i % 3) * 0.5;
            elipses.add(new Elips(a, b));
        }

        long sequentialStart = System.currentTimeMillis();
        for (Elips elips : elipses) {
            elips.hitungLuas();
            elips.hitungKeliling();
        }
        long sequentialEnd = System.currentTimeMillis();

        long sequentialTime = sequentialEnd - sequentialStart;
        System.out.printf("Sequential execution for %d elips objects: %d ms%n", count, sequentialTime);

        List<Thread> threads = new ArrayList<>(count);
        for (Elips elips : elipses) {
            threads.add(elips.createThread());
        }

        long parallelStart = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long parallelEnd = System.currentTimeMillis();
        long parallelTime = parallelEnd - parallelStart;
        System.out.printf("Parallel execution for %d elips objects: %d ms%n", count, parallelTime);
        System.out.printf("Speedup: %.2fx%n", sequentialTime / (double) parallelTime);
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