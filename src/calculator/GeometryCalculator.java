package calculator;

import geometry.*;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.swing.SwingUtilities;

/**
 * Kelas untuk kalkulasi geometri dengan dukungan multithreading
 * Demonstrasi Multithreading
 */
public class GeometryCalculator {
    
    private List<CalculationRecord> history;
    private Thread calculationThread;
    private volatile boolean isCalculating = false;
    private final Object lock = new Object();
    
    public static class CalculationRecord {
        private String shapeName;
        private String parameters;
        private double result;
        private String resultType;
        private Date timestamp;
        
        public CalculationRecord(String shapeName, String parameters, double result, String resultType) {
            this.shapeName = shapeName;
            this.parameters = parameters;
            this.result = result;
            this.resultType = resultType;
            this.timestamp = new Date();
        }
        
        @Override
        public String toString() {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            return String.format("[%s] %s(%s) = %.4f %s",
                sdf.format(timestamp), shapeName, parameters, result, resultType);
        }
        
        public String toFullString() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return String.format("[%s] %s(%s) = %.4f %s",
                sdf.format(timestamp), shapeName, parameters, result, resultType);
        }
        
        public String toCSVFormat() {
            return String.format("%s,%s,%.4f,%s,%s",
                shapeName, parameters, result, resultType, timestamp);
        }
        
        public String getShapeName() { return shapeName; }
        public String getParameters() { return parameters; }
        public double getResult() { return result; }
        public String getResultType() { return resultType; }
        public Date getTimestamp() { return timestamp; }
    }
    
    public GeometryCalculator() {
        this.history = new ArrayList<>();
    }
    
    public interface GeometryCalculationCallback {
        void onProgress(int progress);
        void onComplete(double result, String resultType, String details);
        void onError(String error);
    }
    
    public void calculateAsync(BendaGeometri shape, String parameters, 
                               GeometryCalculationCallback callback) {
        
        calculationThread = new Thread(() -> {
            isCalculating = true;
            
            try {
                for (int i = 0; i <= 100; i += 20) {
                    Thread.sleep(50);
                    if (callback != null) {
                        final int progress = i;
                        SwingUtilities.invokeLater(() -> callback.onProgress(progress));
                    }
                }
                
                double result;
                String resultType;
                
                if (shape instanceof Benda2Dimensi) {
                    result = shape.hitungLuas();
                    resultType = "Luas";
                } else if (shape instanceof Benda3Dimensi) {
                    result = ((Benda3Dimensi) shape).hitungVolume();
                    resultType = "Volume";
                } else {
                    result = shape.hitungLuas();
                    resultType = "Luas";
                }
                
                synchronized (lock) {
                    CalculationRecord record = new CalculationRecord(
                        shape.getNama(), parameters, result, resultType
                    );
                    history.add(record);
                }
                
                if (callback != null) {
                    final double finalResult = result;
                    final String finalResultType = resultType;
                    final String details = shape.info();
                    SwingUtilities.invokeLater(() -> {
                        callback.onComplete(finalResult, finalResultType, details);
                    });
                }
                
            } catch (InterruptedException e) {
                if (callback != null) {
                    SwingUtilities.invokeLater(() -> callback.onError("Perhitungan dibatalkan"));
                }
            } catch (Exception e) {
                if (callback != null) {
                    SwingUtilities.invokeLater(() -> callback.onError("Error: " + e.getMessage()));
                }
            } finally {
                isCalculating = false;
            }
        });
        
        calculationThread.start();
    }
    
    public List<CalculationRecord> getHistory() {
        synchronized (lock) {
            return new ArrayList<>(history);
        }
    }
    
    public void addCalculationRecord(String shapeName, String parameters, double result, String resultType) {
        synchronized (lock) {
            history.add(new CalculationRecord(shapeName, parameters, result, resultType));
        }
    }
    
    public void saveHistoryToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("=== GEOMETRY CALCULATION HISTORY ===");
            writer.newLine();
            writer.write("Generated: " + new Date());
            writer.newLine();
            writer.write("Total perhitungan: " + history.size());
            writer.newLine();
            writer.write("====================================");
            writer.newLine();
            writer.newLine();
            
            synchronized (lock) {
                for (CalculationRecord record : history) {
                    writer.write(record.toFullString());
                    writer.newLine();
                }
            }
            
            writer.newLine();
            writer.write("=== END OF HISTORY ===");
            System.out.println("History saved to: " + filename);
        } catch (IOException e) {
            System.err.println("Failed to save history: " + e.getMessage());
        }
    }
    
    public void clearHistory() {
        synchronized (lock) {
            history.clear();
        }
    }
    
    public void cancelCalculation() {
        if (calculationThread != null && calculationThread.isAlive()) {
            calculationThread.interrupt();
            isCalculating = false;
        }
    }
    
    public boolean isCalculating() {
        return isCalculating;
    }
    
    public static String formatParameters(Object... params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(String.format("%.2f", (Double) params[i]));
        }
        return sb.toString();
    }
} 
