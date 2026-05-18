package ui;

import geometry.*;
import javax.swing.*;
import java.awt.*;

/**
 * Panel DEMO POLYMORPHISM - Khusus untuk melihat polimorfisme di terminal
 * Output perhitungan dan demonstrasi polimorfisme ditampilkan di CONSOLE/TERMINAL
 * BUKAN di GUI, agar konsep polimorfisme terlihat jelas seperti contoh dosen
 */
public class DemoPolymorphismPanel extends JPanel {

    private JTextArea demoArea;
    private JButton btnDemo1, btnDemo2, btnDemo3, btnClear;

    public DemoPolymorphismPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel(
            "<html><h2>DEMONSTRASI POLYMORPHISM</h2>" +
            "<p>Super Class: <b>BendaGeometri</b> | Subclass: Elips, TabungElips, BolaElips, dll</p>" +
            "<p>Perhatikan OUTPUT di <b style='color:red'>TERMINAL/CONSOLE</b> untuk melihat polimorfisme!</p></html>"
        );
        header.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        header.setBorder(BorderFactory.createEmptyBorder(5, 5, 15, 5));
        add(header, BorderLayout.NORTH);

        demoArea = new JTextArea();
        demoArea.setEditable(false);
        demoArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        demoArea.setBackground(new Color(240, 248, 255));
        demoArea.setText(
            "=== DEMO POLYMORPHISM ===\n\n" +
            "Klik tombol di bawah untuk menjalankan demonstrasi.\n" +
            "Semua OUTPUT AKAN MUNCUL DI TERMINAL/CONSOLE.\n\n" +
            "Perhatikan bahwa:\n" +
            "✓ Variabel bertipe BendaGeometri bisa menampung berbagai subclass\n" +
            "✓ Method yang dipanggil (info(), hitungLuas()) akan BERBEDA hasilnya\n" +
            "✓ Satu tipe reference, banyak bentuk (banyak perilaku)\n"
        );
        add(new JScrollPane(demoArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));

        btnDemo1 = new JButton("Demo 1: Basic Polymorphism");
        btnDemo1.setBackground(new Color(56, 130, 242));
        btnDemo1.setForeground(Color.WHITE);
        btnDemo1.addActionListener(e -> demoBasicPolymorphism());

        btnDemo2 = new JButton("Demo 2: Array of Shapes");
        btnDemo2.setBackground(new Color(56, 130, 242));
        btnDemo2.setForeground(Color.WHITE);
        btnDemo2.addActionListener(e -> demoArrayPolymorphism());

        btnDemo3 = new JButton("Demo 3: Method Parameter");
        btnDemo3.setBackground(new Color(56, 130, 242));
        btnDemo3.setForeground(Color.WHITE);
        btnDemo3.addActionListener(e -> demoMethodParameterPolymorphism());

        btnClear = new JButton("Console Hint");
        btnClear.setBackground(new Color(100, 100, 100));
        btnClear.setForeground(Color.WHITE);
        btnClear.addActionListener(e -> demoArea.append("\n\n>>> Lihat output di TERMINAL/CONSOLE <<<\n"));

        buttonPanel.add(btnDemo1);
        buttonPanel.add(btnDemo2);
        buttonPanel.add(btnDemo3);
        buttonPanel.add(btnClear);

        add(buttonPanel, BorderLayout.SOUTH);

        // Cetak instruksi di terminal
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║     DEMO POLYMORPHISM - BendaGeometri                          ║");
        System.out.println("║     Semua output demonstrasi akan tampil di sini (terminal)    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private void demoBasicPolymorphism() {
        System.out.println();
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ DEMO 1: BASIC POLYMORPHISM                                  │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        System.out.println();

        BendaGeometri shape1; // tipe super class

        System.out.println("1. BendaGeometri shape1 = new Elips(7, 4);");
        shape1 = new Elips(7, 4);
        System.out.println("   >> " + shape1.getClass().getSimpleName() + " | " + shape1.getNama());
        System.out.println("   >> info(): " + shape1.info());
        System.out.println("   >> hitungLuas(): " + String.format("%.4f", shape1.hitungLuas()));
        System.out.println();

        System.out.println("2. BendaGeometri shape2 = new TabungElips(7, 4, 10);");
        BendaGeometri shape2 = new TabungElips(7, 4, 10);
        System.out.println("   >> " + shape2.getClass().getSimpleName() + " | " + shape2.getNama());
        System.out.println("   >> info(): " + shape2.info());
        System.out.println("   >> hitungLuas(): " + String.format("%.4f", shape2.hitungLuas()));
        System.out.println();

        System.out.println("3. BendaGeometri shape3 = new BolaElips(5,4,6);");
        BendaGeometri shape3 = new BolaElips(5, 4, 6);
        System.out.println("   >> " + shape3.getClass().getSimpleName() + " | " + shape3.getNama());
        System.out.println("   >> info(): " + shape3.info());
        System.out.println("   >> hitungLuas(): " + String.format("%.4f", shape3.hitungLuas()));
        System.out.println();

        System.out.println("4. BendaGeometri shape4 = new KerucutElips(7,4,12);");
        BendaGeometri shape4 = new KerucutElips(7, 4, 12);
        System.out.println("   >> " + shape4.getClass().getSimpleName() + " | " + shape4.getNama());
        System.out.println("   >> info(): " + shape4.info());
        System.out.println("   >> hitungLuas(): " + String.format("%.4f", shape4.hitungLuas()));
        System.out.println();

        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("✓ KESIMPULAN: Meskipun semua variabel bertipe BendaGeometri,");
        System.out.println("  method info() dan hitungLuas() menghasilkan PERILAKU BERBEDA");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();

        SwingUtilities.invokeLater(() -> {
            demoArea.append("\n[Demo 1 Selesai] Lihat terminal untuk hasil lengkap!\n");
            demoArea.setCaretPosition(demoArea.getDocument().getLength());
        });
    }

    private void demoArrayPolymorphism() {
        System.out.println();
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ DEMO 2: POLYMORPHISM DENGAN ARRAY                           │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        System.out.println();

        BendaGeometri[] shapes = new BendaGeometri[5];
        shapes[0] = new Elips(10, 5);
        shapes[1] = new TabungElips(10, 5, 8);
        shapes[2] = new BolaElips(6, 6, 6);
        shapes[3] = new KerucutElips(8, 5, 15);
        shapes[4] = new KerucutTerpancung(10, 6, 5, 3);

        for (int i = 0; i < shapes.length; i++) {
            BendaGeometri shape = shapes[i];
            System.out.printf("[%d] %s\n", i, shape.getClass().getSimpleName());
            System.out.printf("    %s\n", shape.info());
            System.out.printf("    Hasil hitungLuas(): %.4f\n", shape.hitungLuas());
            System.out.println();
        }

        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("✓ KESIMPULAN: Satu array dengan tipe super class bisa memuat");
        System.out.println("  berbagai objek subclass, dan method yang dipanggil akan");
        System.out.println("  BERBEDA sesuai dengan jenis objek ASLINYA");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();

        SwingUtilities.invokeLater(() -> {
            demoArea.append("\n[Demo 2 Selesai] Lihat terminal untuk hasil lengkap!\n");
            demoArea.setCaretPosition(demoArea.getDocument().getLength());
        });
    }

    private void demoMethodParameterPolymorphism() {
        System.out.println();
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ DEMO 3: POLYMORPHISM SEBAGAI PARAMETER METHOD               │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        System.out.println();

        demoPrintInfo(new Elips(8, 5), "Elips");
        demoPrintInfo(new TabungElips(8, 5, 12), "TabungElips");
        demoPrintInfo(new BolaElips(7, 5, 4), "BolaElips");
        demoPrintInfo(new KerucutElips(6, 4, 10), "KerucutElips");
        demoPrintInfo(new CincinElips(8, 3, 5), "CincinElips");

        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("✓ KESIMPULAN: Method dengan parameter SUPER CLASS bisa");
        System.out.println("  menerima berbagai objek SUBCLASS. Di dalam method, ");
        System.out.println("  pemanggilan method akan menyesuaikan dengan objek asli.");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();

        SwingUtilities.invokeLater(() -> {
            demoArea.append("\n[Demo 3 Selesai] Lihat terminal untuk hasil lengkap!\n");
            demoArea.setCaretPosition(demoArea.getDocument().getLength());
        });
    }

    private void demoPrintInfo(BendaGeometri shape, String nama) {
        System.out.println(">>> Memanggil demoPrintInfo dengan objek " + nama);
        System.out.println("    Parameter shape bertipe: BendaGeometri (super class)");
        System.out.println("    Objek asli yang dikirim: " + shape.getClass().getSimpleName());
        System.out.println("    shape.getNama()  : " + shape.getNama());
        System.out.println("    shape.info()     : " + shape.info());
        System.out.println("    shape.hitungLuas(): " + String.format("%.4f", shape.hitungLuas()));
        System.out.println();
    }
}
