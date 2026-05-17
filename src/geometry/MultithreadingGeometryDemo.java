package geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Demo penggunaan multithreading pada koleksi objek geometri.
// Menunjukkan konsep berikut:
// - pembuatan thread pool dengan ExecutorService
// - eksekusi Runnable dan Callable
// - penggunaan Future untuk menunggu hasil
// - pembatalan tugas dengan interrupt/cancel
// - perbandingan waktu eksekusi sekuensial vs paralel
public class MultithreadingGeometryDemo {

    public static void main(String[] args) {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("=== MULTITHREADING GEOMETRY DEMO ===");
        System.out.printf("Available processor cores: %d%n", cores);
        System.out.println();

        List<List<Runnable>> groups = new ArrayList<>();

        groups.add(List.of(new Elips(4.0, 2.0), new Elips(6.0, 3.0)));
        groups.add(List.of(new TabungElips(4.0, 2.0, 5.0), new TabungElips(3.0, 1.5, 4.0)));
        groups.add(List.of(new KerucutElips(4.0, 2.0, 5.0), new KerucutElips(3.0, 3.0, 4.0)));
        groups.add(List.of(new BolaElips(3.0, 2.0, 1.5), new BolaElips(4.0, 3.0, 2.0)));
        groups.add(List.of(new KerucutTerpancung(4.0, 2.0, 5.0, 1.0), new KerucutTerpancung(3.0, 3.0, 4.0, 0.5)));
        groups.add(List.of(new Juring(2.5, Math.PI / 2.0, 1.0), new Juring(3.0, Math.PI / 3.0, 1.2)));
        groups.add(List.of(new Tembereng(1.0, 2.0), new Tembereng(1.5, 2.5)));
        groups.add(List.of(new CincinElips(3.5, 1.2, 1.0), new CincinElips(4.0, 1.5, 1.2)));

        // Pool fixed berdasarkan jumlah core untuk memanfaatkan paralelisme CPU
        ExecutorService pool = Executors.newFixedThreadPool(cores);
        List<Future<?>> previousGroupFutures = null;

        System.out.println("=== MENJALANKAN OBJEK GEOMETRI DENGAN INTERRUPT BERURUTAN ANTAR GROUP ===");
        for (int cycle = 1; cycle <= 2; cycle++) {
            System.out.printf("=== CYCLE %d ===%n", cycle);
            for (int groupIndex = 0; groupIndex < groups.size(); groupIndex++) {
                List<Runnable> group = groups.get(groupIndex);
                System.out.printf("--- Start group %d: %s --- %n", groupIndex + 1, group.get(0).getClass().getSimpleName());

                if (previousGroupFutures != null) {
                    for (Future<?> future : previousGroupFutures) {
                        if (!future.isDone()) {
                            future.cancel(true);
                        }
                    }
                }

                List<Future<?>> currentFutures = new ArrayList<>();
                // Submit masing-masing shape sebagai tugas terpisah
                for (Runnable shape : group) {
                    currentFutures.add(pool.submit(shape));
                }

                // Tunda sebelum meng-interrupt group sebelumnya untuk melihat efek interupsi
                try {
                    Thread.sleep(2000); // delay 2 seconds before next group interrupt
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                previousGroupFutures = currentFutures;
            }
        }

        if (previousGroupFutures != null) {
            for (Future<?> future : previousGroupFutures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    // ignore; this may be interrupted by later groups
                }
            }
        }

        pool.shutdownNow();
        System.out.println("=== SEMUA OBJEK SELESAI ===\n");

        runLargeScaleComparison(cores);
    }

    private static void runLargeScaleComparison(int cores) {
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

        // Perbandingan waktu sekuensial vs paralel
        ExecutorService parallelPool = Executors.newFixedThreadPool(cores);
        List<Future<Double>> futures = new ArrayList<>(count);

        long parallelStart = System.currentTimeMillis();
        for (Elips elips : elipses) {
            futures.add(parallelPool.submit((Callable<Double>) () -> {
                elips.run();
                return elips.hitungLuas();
            }));
        }

        for (Future<Double> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        long parallelEnd = System.currentTimeMillis();

        parallelPool.shutdown();
        long parallelTime = parallelEnd - parallelStart;
        System.out.printf("Parallel execution for %d elips objects: %d ms%n", count, parallelTime);
        System.out.printf("Speedup: %.2fx%n", sequentialTime / (double) parallelTime);
    }
}
