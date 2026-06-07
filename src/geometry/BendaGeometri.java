 
package geometry;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Abstract Class - Demonstrasi Abstract Class
 * Kelas induk abstrak untuk semua benda geometri
 */
public abstract class BendaGeometri implements Runnable {
    
    // Attributes made public per refactor
    public String nama;  // hiding information yang will still have synchronized accessors
    public static int totalObjek = 0;
    public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    
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
        log(String.format("%s: mulai menghitung...", namaShape));
        try {
            // Delay 2 seconds so the process is clearly visible
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            log(String.format("%s: terinterupsi saat menghitung", namaShape));
            Thread.currentThread().interrupt();
            return;
        }

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

    public Thread createThread() {
        Thread thread = new Thread(this, getNama() + "-Thread");
        thread.setDaemon(true);
        return thread;
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
                    Thread thread = shape.createThread();
                    thread.start();
                    activeThreads.add(thread);
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