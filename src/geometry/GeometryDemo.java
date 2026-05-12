package geometry;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;

/**
 * Main program demonstrating OOP concepts and multithreading
 */
public class GeometryDemo {

    public static void main(String[] args) {
        // Create list of shapes (Polymorphism)
        List<EllipticSolid> shapes = new ArrayList<>();
        shapes.add(new Cone(3.0, 3.0, 5.0)); // Circle case
        shapes.add(new Cone(4.0, 2.0, 5.0)); // Ellipse case
        shapes.add(new FrustumCone(4.0, 2.0, 3.0, 1.5, 5.0));
        shapes.add(new Cylinder(3.0, 3.0, 5.0)); // Circle
        shapes.add(new Cylinder(4.0, 2.0, 5.0)); // Ellipse
        shapes.add(new Ellipsoid(3.0, 3.0, 3.0)); // Sphere
        shapes.add(new Ellipsoid(4.0, 2.0, 3.0)); // Ellipsoid
        shapes.add(new EllipticSector(4.0, 2.0, 3.0, Math.PI));
        shapes.add(new EllipticSegment(4.0, 2.0, 3.0, 1.5));
        shapes.add(new EllipticAnnulus(4.0, 2.0, 2.0, 1.0, 3.0));

        // Sequential calculation
        System.out.println("=== SEQUENTIAL CALCULATION ===");
        for (EllipticSolid shape : shapes) {
            System.out.println(shape.getInfo());
        }

        // Parallel calculation using multithreading
        System.out.println("\n=== PARALLEL CALCULATION ===");
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Future<String>> futures = new ArrayList<>();
        for (EllipticSolid shape : shapes) {
            Callable<String> task = () -> shape.getInfo();
            Future<String> future = executor.submit(task);
            futures.add(future);
        }

        for (Future<String> future : futures) {
            try {
                System.out.println(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        // Demonstrate method overloading (if needed)
        System.out.println("\n=== METHOD OVERLOADING EXAMPLE ===");
        Cone cone1 = new Cone(3.0, 3.0, 5.0);
        Cone cone2 = new Cone(4.0, 2.0, 5.0, "Blue");
        System.out.println("Cone 1: " + cone1.getInfo());
        System.out.println("Cone 2: " + cone2.getInfo());
    }
}