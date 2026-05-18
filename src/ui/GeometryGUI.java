package ui;

import geometry.*;
import calculator.GeometryCalculator;
import exceptions.GeometryException;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * GUI Utama Aplikasi Perhitungan Geometri
 * Demonstrasi Event Handling dan Sistem Menu
 */
public class GeometryGUI implements ActionListener {

    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JTextArea resultArea;
    private JTextArea historyArea;
    private JProgressBar progressBar;
    private JLabel statusLabel;

    private JTextField tfSumbuPanjang, tfSumbuPendek, tfTinggi;
    private JTextField tfJariJariAtas, tfJariJariDalam, tfJariJariLuar;
    private JTextField tfSumbuX, tfSumbuY, tfSumbuZ;
    private JTextField tfSudut, tfRadiusBola;
    private JCheckBox cbUseElipsBaseForKerucut, cbUseElipsBaseForKerucutTerp, cbUseElipsBaseForTabung;
    // History UI model
    private DefaultListModel<String> historyListModel;
    private JList<String> historyList;
    // Menu items (declared here so actionPerformed can access them)
    private JMenuItem exitItem, exportItem, clearHistoryItem, openHistoryItem, aboutItem;
    private JCheckBoxMenuItem showStatusItem;
    // Optional per-shape threading checkbox (may be present in UI)
    private JCheckBox cbUseShapeThreading;
    // Calculator backend
    private GeometryCalculator calculator;
    // Opsi UI: jika dipilih, gunakan API per-shape async/future

    public GeometryGUI() {
        frame = new JFrame("Geometry Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);

        // Backend calculator instance (used by history and async calculations)
        calculator = new GeometryCalculator();

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Elips", create2DPanel());
        tabbedPane.addTab("Kerucut", create3DLimasPanel());
        tabbedPane.addTab("Kerucut Terpancung", createKerucutTerpancungPanel());
        tabbedPane.addTab("Tabung", createTabungElipsPanel());
        tabbedPane.addTab("Bola", createBolaElipsPanel());
        tabbedPane.addTab("Juring", createJuringPanel());
        tabbedPane.addTab("Tembereng", createTemberengPanel());
        tabbedPane.addTab("Cincin", createCincinPanel());
        tabbedPane.addTab("Polymorphism", createPolymorphismPanel());
        tabbedPane.addTab("Polymorphism Demo", createPolymorphismDemoPanel());
        tabbedPane.addTab("History", createHistoryPanel());

        // Result area panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(new Color(255, 255, 255));

        resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        resultArea.setBackground(new Color(250, 250, 255));

        // Redirect System.out/err to resultArea so background thread logs appear in GUI
        redirectSystemStreams();

        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultPanel.add(resultScroll, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        bottomPanel.add(progressBar, BorderLayout.CENTER);
        statusLabel = new JLabel("Siap melakukan perhitungan");
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JPanel create2DPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Baris 0: Sumbu Panjang
    gbc.gridx = 0; gbc.gridy = 0;
    gbc.weightx = 0;  // label tidak butuh weight
    panel.add(new JLabel("Sumbu Panjang (a):"), gbc);
    gbc.gridx = 1;
    gbc.weightx = 1;  // textfield mengambil ruang horizontal
    tfSumbuPanjang = new JTextField(10);
    panel.add(tfSumbuPanjang, gbc);

    // Baris 1: Sumbu Pendek
    gbc.gridx = 0; gbc.gridy = 1;
    gbc.weightx = 0;
    panel.add(new JLabel("Sumbu Pendek (b):"), gbc);
    gbc.gridx = 1;
    gbc.weightx = 1;
    tfSumbuPendek = new JTextField(10);
    panel.add(tfSumbuPendek, gbc);

    // Baris 2: Tombol
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    JButton btnHitung = new JButton("Hitung");
    JButton btnReset = new JButton("Reset");
    btnHitung.addActionListener(e -> calculateElips());
    btnReset.addActionListener(e -> {
        tfSumbuPanjang.setText("");
        tfSumbuPendek.setText("");
    });
    buttonPanel.add(btnHitung);
    buttonPanel.add(btnReset);

    gbc.gridx = 0; gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.weighty = 0;  // tidak makan ruang vertikal ekstra
    panel.add(buttonPanel, gbc);

    // Baris 3: Info Panel - PERBAIKAN: tambah weighty
    JPanel infoPanel = createInfoPanel("Elips",
        "Rumus:\n• Luas = π × a × b\n• Keliling ≈ π × [3(a+b) - √((3a+b)(a+3b))]",
        "Keterangan:\na = sumbu panjang\nb = sumbu pendek");

    gbc.gridy = 3;
    gbc.weighty = 1.0;     // ← INI PENTING: beri ruang vertikal
    gbc.fill = GridBagConstraints.BOTH;  // ← biar mengisi penuh
    panel.add(infoPanel, gbc);

    return panel;
}

    private JPanel create3DLimasPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Sumbu Panjang Alas (a):"), gbc);
        gbc.gridx = 1;
        JTextField tfAlasPanjang = new JTextField(10);
        panel.add(tfAlasPanjang, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Sumbu Pendek Alas (b):"), gbc);
        gbc.gridx = 1;
        JTextField tfAlasPendek = new JTextField(10);
        panel.add(tfAlasPendek, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Tinggi Kerucut (t):"), gbc);
        gbc.gridx = 1;
        JTextField tfTinggi = new JTextField(10);
        panel.add(tfTinggi, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        cbUseElipsBaseForKerucut = new JCheckBox("Gunakan data Elips dari panel Elips", true);
        panel.add(cbUseElipsBaseForKerucut, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Override Alas (a):"), gbc);
        gbc.gridx = 1;
        tfAlasPanjang.setEnabled(false);
        panel.add(tfAlasPanjang, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Override Alas (b):"), gbc);
        gbc.gridx = 1;
        tfAlasPendek.setEnabled(false);
        panel.add(tfAlasPendek, gbc);

        cbUseElipsBaseForKerucut.addActionListener(e -> {
            boolean usePanel = cbUseElipsBaseForKerucut.isSelected();
            tfAlasPanjang.setEnabled(!usePanel);
            tfAlasPendek.setEnabled(!usePanel);
            if (usePanel) {
                tfAlasPanjang.setText("");
                tfAlasPendek.setText("");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JButton btnHitung = new JButton("Hitung");
        btnHitung.addActionListener(e -> {
            try {
                double t = Double.parseDouble(tfTinggi.getText().trim());
                Elips alas = resolveElipsBase(cbUseElipsBaseForKerucut.isSelected(), tfAlasPanjang, tfAlasPendek);
                calculateKerucutElips(alas, t);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka yang valid dan lebih dari 0!");
            }
        });
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);
        panel.add(btnHitung, gbc);

        return panel;
    }

    private JPanel createKerucutTerpancungPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Sumbu Panjang Alas (a):"), gbc);
        gbc.gridx = 1;
        JTextField tfAlasPanjang = new JTextField(10);
        panel.add(tfAlasPanjang, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Sumbu Pendek Alas (b):"), gbc);
        gbc.gridx = 1;
        JTextField tfAlasPendek = new JTextField(10);
        panel.add(tfAlasPendek, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Tinggi (t):"), gbc);
        gbc.gridx = 1;
        JTextField tfTinggi = new JTextField(10);
        panel.add(tfTinggi, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Jari-jari Atas (r):"), gbc);
        gbc.gridx = 1;
        tfJariJariAtas = new JTextField(10);
        panel.add(tfJariJariAtas, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        cbUseElipsBaseForKerucutTerp = new JCheckBox("Gunakan data Elips dari panel Elips", true);
        panel.add(cbUseElipsBaseForKerucutTerp, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Override Alas (a):"), gbc);
        gbc.gridx = 1;
        tfAlasPanjang.setEnabled(false);
        panel.add(tfAlasPanjang, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Override Alas (b):"), gbc);
        gbc.gridx = 1;
        tfAlasPendek.setEnabled(false);
        panel.add(tfAlasPendek, gbc);

        cbUseElipsBaseForKerucutTerp.addActionListener(e -> {
            boolean usePanel = cbUseElipsBaseForKerucutTerp.isSelected();
            tfAlasPanjang.setEnabled(!usePanel);
            tfAlasPendek.setEnabled(!usePanel);
            if (usePanel) {
                tfAlasPanjang.setText("");
                tfAlasPendek.setText("");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        JButton btnHitung = new JButton("Hitung");
        btnHitung.addActionListener(e -> {
            try {
                double t = Double.parseDouble(tfTinggi.getText().trim());
                double r = Double.parseDouble(tfJariJariAtas.getText().trim());
                Elips alas = resolveElipsBase(cbUseElipsBaseForKerucutTerp.isSelected(), tfAlasPanjang, tfAlasPendek);
                calculateKerucutTerpancung(alas.getSumbuPanjang(), alas.getSumbuPendek(), t, r);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka yang valid dan lebih dari 0!");
            }
        });
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);
        panel.add(btnHitung, gbc);

        return panel;
    }

    private JPanel createTabungElipsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Sumbu Panjang Alas (a):"), gbc);
        gbc.gridx = 1;
        JTextField tfA = new JTextField(10);
        panel.add(tfA, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Sumbu Pendek Alas (b):"), gbc);
        gbc.gridx = 1;
        JTextField tfB = new JTextField(10);
        panel.add(tfB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Tinggi Tabung (t):"), gbc);
        gbc.gridx = 1;
        JTextField tfT = new JTextField(10);
        panel.add(tfT, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        cbUseElipsBaseForTabung = new JCheckBox("Gunakan data Elips dari panel Elips", true);
        panel.add(cbUseElipsBaseForTabung, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Override Alas (a):"), gbc);
        gbc.gridx = 1;
        tfA.setEnabled(false);
        panel.add(tfA, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Override Alas (b):"), gbc);
        gbc.gridx = 1;
        tfB.setEnabled(false);
        panel.add(tfB, gbc);

        cbUseElipsBaseForTabung.addActionListener(e -> {
            boolean usePanel = cbUseElipsBaseForTabung.isSelected();
            tfA.setEnabled(!usePanel);
            tfB.setEnabled(!usePanel);
            if (usePanel) {
                tfA.setText("");
                tfB.setText("");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JButton btnHitung = new JButton("Hitung");
        btnHitung.addActionListener(e -> {
            try {
                double t = Double.parseDouble(tfT.getText().trim());
                Elips alas = resolveElipsBase(cbUseElipsBaseForTabung.isSelected(), tfA, tfB);
                calculateTabungElips(alas, t);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka yang valid dan lebih dari 0!");
            }
        });
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);
        panel.add(btnHitung, gbc);

        return panel;
    }

    private JPanel createBolaElipsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Radius Sumbu X (a):"), gbc);
        gbc.gridx = 1;
        tfSumbuX = new JTextField(10);
        panel.add(tfSumbuX, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Radius Sumbu Y (b):"), gbc);
        gbc.gridx = 1;
        tfSumbuY = new JTextField(10);
        panel.add(tfSumbuY, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Radius Sumbu Z (c):"), gbc);
        gbc.gridx = 1;
        tfSumbuZ = new JTextField(10);
        panel.add(tfSumbuZ, gbc);

        JButton btnHitung = new JButton("Hitung");
        btnHitung.addActionListener(e -> {
            try {
                double x = Double.parseDouble(tfSumbuX.getText());
                double y = Double.parseDouble(tfSumbuY.getText());
                double z = Double.parseDouble(tfSumbuZ.getText());
                calculateBolaElips(x, y, z);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka!");
            }
        });
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(btnHitung, gbc);

        return panel;
    }

    private JPanel createPolymorphismDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoArea.setBackground(new Color(245, 245, 255));
        infoArea.setText("Polymorphism Demo\n\n" +
                "Contoh ini dibuat sesuai project tetapi mengikuti konsep dosen:\n" +
                "satu super class dapat merujuk ke banyak subclass.\n\n" +
                "Objek yang digunakan:\n" +
                "EkspresiWajah objEkspresi = new EkspresiWajah();\n" +
                "EkspresiWajah objGembira = new WajahGembira();\n" +
                "EkspresiWajah objSedih = new WajahSedih();\n" +
                "EkspresiWajah objMarah = new WajahMarah();\n\n" +
                "Semua objek tersebut diteruskan ke method yang sama,\n" +
                "tetapi hasil respons() berbeda sesuai subclass.");

        JButton btnRun = new JButton("Run Polymorphism Demo");
        btnRun.setBackground(new Color(56, 130, 242));
        btnRun.setForeground(Color.WHITE);
        btnRun.setFocusPainted(false);
        btnRun.addActionListener(e -> runPolymorphismDemo());

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(btnRun, BorderLayout.WEST);
        topPanel.add(new JLabel("Output akan muncul di panel Hasil Perhitungan."), BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(infoArea), BorderLayout.CENTER);

        return panel;
    }

    private void runPolymorphismDemo() {
        resultArea.setText("");
        statusLabel.setText("Menjalankan demo polimorfisme...");
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);

        new Thread(() -> {
            try {
                System.out.println("=== POLYMORPHISM DEMO ===");
                System.out.println("Sama seperti contoh dosen: Parent p = new Child();");
                System.out.println();

                EkspresiWajah objEkspresi = new EkspresiWajah();
                EkspresiWajah objGembira = new WajahGembira();
                EkspresiWajah objSedih = new WajahSedih();
                EkspresiWajah objMarah = new WajahMarah();

                EkspresiWajah[] arrEkspresi = new EkspresiWajah[4];
                arrEkspresi[0] = objEkspresi;
                arrEkspresi[1] = objGembira;
                arrEkspresi[2] = objSedih;
                arrEkspresi[3] = objMarah;

                for (int i = 0; i < arrEkspresi.length; i++) {
                    System.out.printf("Ekspresi[%d]: %s%n", i, arrEkspresi[i].respons());
                }

                System.out.println();
                System.out.println("Penjelasan:");
                System.out.println("- Semua variabel bertipe EkspresiWajah.");
                System.out.println("- Setiap objek memiliki perilaku respons() masing-masing.");
                System.out.println("- Ini adalah contoh polymorphism: satu tipe, banyak bentuk.");
            } finally {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(false);
                    progressBar.setIndeterminate(false);
                    statusLabel.setText("Demo polimorfisme selesai.");
                });
            }
        }).start();
    }

    private static class EkspresiWajah {
        protected String nama;

        public EkspresiWajah() {
            this.nama = "Netral";
        }

        public String respons() {
            return "Ekspresi " + nama;
        }
    }

    private static class WajahGembira extends EkspresiWajah {
        public WajahGembira() {
            this.nama = "Gembira";
        }

        @Override
        public String respons() {
            return "Ekspresi " + nama + " :-)";
        }
    }

    private static class WajahSedih extends EkspresiWajah {
        public WajahSedih() {
            this.nama = "Sedih";
        }

        @Override
        public String respons() {
            return "Ekspresi " + nama + " :-(";
        }
    }

    private static class WajahMarah extends EkspresiWajah {
        public WajahMarah() {
            this.nama = "Marah";
        }

        @Override
        public String respons() {
            return "Ekspresi " + nama + " >:(";
        }
    }

    private JPanel createJuringPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Jari-jari (r):"), gbc);
        gbc.gridx = 1;
        JTextField tfR = new JTextField(10);
        panel.add(tfR, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Sudut (derajat):"), gbc);
        gbc.gridx = 1;
        tfSudut = new JTextField(10);
        panel.add(tfSudut, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Tinggi (t):"), gbc);
        gbc.gridx = 1;
        JTextField tfT = new JTextField(10);
        panel.add(tfT, gbc);

        JButton btnHitung = new JButton("Hitung");
        btnHitung.addActionListener(e -> {
            try {
                double r = Double.parseDouble(tfR.getText());
                double sudut = Double.parseDouble(tfSudut.getText());
                double t = Double.parseDouble(tfT.getText());
                calculateJuring(r, Math.toRadians(sudut), t);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka!");
            }
        });
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(btnHitung, gbc);

        return panel;
    }

    private JPanel createTemberengPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Tinggi Tembereng (t):"), gbc);
        gbc.gridx = 1;
        JTextField tfT = new JTextField(10);
        panel.add(tfT, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Radius Bola (R):"), gbc);
        gbc.gridx = 1;
        tfRadiusBola = new JTextField(10);
        panel.add(tfRadiusBola, gbc);

        JButton btnHitung = new JButton("Hitung");
        btnHitung.addActionListener(e -> {
            try {
                double t = Double.parseDouble(tfT.getText());
                double R = Double.parseDouble(tfRadiusBola.getText());
                calculateTembereng(t, R);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka!");
            }
        });
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(btnHitung, gbc);

        return panel;
    }

    private JPanel createCincinPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Jari-jari Luar (R):"), gbc);
        gbc.gridx = 1;
        tfJariJariLuar = new JTextField(10);
        panel.add(tfJariJariLuar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Jari-jari Dalam (r):"), gbc);
        gbc.gridx = 1;
        tfJariJariDalam = new JTextField(10);
        panel.add(tfJariJariDalam, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Tinggi (t):"), gbc);
        gbc.gridx = 1;
        JTextField tfT = new JTextField(10);
        panel.add(tfT, gbc);

        JButton btnHitung = new JButton("Hitung");
        btnHitung.addActionListener(e -> {
            try {
                double R = Double.parseDouble(tfJariJariLuar.getText());
                double r = Double.parseDouble(tfJariJariDalam.getText());
                double t = Double.parseDouble(tfT.getText());
                calculateCincin(R, r, t);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka!");
            }
        });
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(btnHitung, gbc);

        return panel;
    }

    private JPanel createPolymorphismPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel desc = new JLabel("Demo Polimorfisme menggunakan objek `BendaGeometri`. Output muncul di panel hasil/terminal.");
        panel.add(desc, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        JCheckBox cbParallel = new JCheckBox("Jalankan paralel (shape-level threading)", false);
        panel.add(cbParallel, gbc);

        gbc.gridx = 1;
        JButton btnRun = new JButton("Run Polymorphism Demo");
        btnRun.setBackground(new Color(56, 130, 242));
        btnRun.setForeground(Color.BLACK);
        btnRun.setFocusPainted(false);
        panel.add(btnRun, gbc);

        btnRun.addActionListener(e -> {
            btnRun.setEnabled(false);
            statusLabel.setText("Menjalankan demo polimorfisme...");

            new Thread(() -> {
                try {
                    // Siapkan koleksi shapes sebagai BendaGeometri (polymorphism)
                    java.util.List<BendaGeometri> shapes = java.util.List.of(
                            new Elips(4.0, 2.0),
                            new TabungElips(new Elips(3.0, 1.5), 4.0),
                            new KerucutElips(4.0, 2.0, 5.0),
                            new KerucutTerpancung(4.0, 2.0, 5.0, 1.0),
                            new BolaElips(3.0, 2.0, 1.5),
                            new Juring(2.5, Math.PI / 2.0, 1.0),
                            new Tembereng(1.0, 2.0),
                            new CincinElips(3.5, 1.2, 1.0)
                    );

                    System.out.println("=== POLYMORPHISM DEMO (BendaGeometri) ===");
                    System.out.println("Menjalankan " + (cbParallel.isSelected() ? "paralel" : "sekuensial") + " untuk contoh shapes\n");

                    if (cbParallel.isSelected()) {
                        java.util.List<java.util.concurrent.Future<Double>> futures = new java.util.ArrayList<>();
                        for (BendaGeometri s : shapes) {
                            System.out.println("Submit: " + s.getNama());
                            futures.add(s.calculateWithFuture());
                        }

                        for (int i = 0; i < shapes.size(); i++) {
                            try {
                                Double res = futures.get(i).get();
                                BendaGeometri s = shapes.get(i);
                                if (s instanceof VolumeCalculable) {
                                    System.out.printf("Result - %s: volume=%.4f\n", s.getNama(), res);
                                } else {
                                    System.out.printf("Result - %s: luas=%.4f\n", s.getNama(), res);
                                }
                            } catch (Exception ex) {
                                System.out.println("Task interrupted/failed: " + ex.getMessage());
                            }
                        }
                    } else {
                        for (BendaGeometri s : shapes) {
                            System.out.println("Run: " + s.getNama());
                            // memanggil method lewat tipe parent -> polymorphism
                            if (s instanceof VolumeCalculable) {
                                double v = ((VolumeCalculable) s).hitungVolume();
                                System.out.printf("Result - %s: volume=%.4f\n", s.getNama(), v);
                            } else {
                                double lu = s.hitungLuas();
                                System.out.printf("Result - %s: luas=%.4f\n", s.getNama(), lu);
                            }
                        }
                    }

                    System.out.println("=== DEMO SELESAI ===\n");
                } finally {
                    SwingUtilities.invokeLater(() -> {
                        btnRun.setEnabled(true);
                        statusLabel.setText("Siap melakukan perhitungan");
                    });
                }
            }).start();
        });

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        historyListModel = new DefaultListModel<>();
        historyList = new JList<>(historyListModel);
        historyList.setFont(new Font("Consolas", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(historyList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnClear = new JButton("Clear History");
        JButton btnExport = new JButton("Export History");

        btnRefresh.addActionListener(e -> loadHistory());
        btnClear.addActionListener(e -> clearHistory());
        btnExport.addActionListener(e -> exportToFile());

        btnRefresh.setBackground(new Color(0, 130, 242));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFocusPainted(true);

        btnClear.setBackground(new Color(220, 0, 0));
        btnClear.setForeground(Color.BLACK);
        btnClear.setFocusPainted(true);

        btnExport.setBackground(new Color(0, 175, 0));
        btnExport.setForeground(Color.BLACK);
        btnExport.setFocusPainted(true);

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnExport);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadHistory();

        return panel;
    }

    private JPanel createAboutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 251, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea aboutText = new JTextArea();
        aboutText.setEditable(false);
        aboutText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        aboutText.setBackground(new Color(245, 245, 245));
        aboutText.setText("""
                ================================================
                        APLIKASI PERHITUNGAN BENDA GEOMETRI
                ================================================

                Aplikasi ini dapat menghitung berbagai benda geometri:

                ★ Benda 2 Dimensi:
                  • Elips

                ★ Benda 3 Dimensi (Limas):
                  • Kerucut dengan alas elips
                  • Kerucut terpancung dengan alas elips

                ★ Benda 3 Dimensi (Prisma):
                  • Tabung dengan alas elips
                  • Bola elips (Ellipsoid)
                  • Juring bola
                  • Tembereng bola
                  • Cincin dengan pola dasar elips (Torus)

                © 2026 - Proyek PBO Kelompok 3
                Versi 1.0""");

        JScrollPane scroll = new JScrollPane(aboutText);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void showHistoryDialog() {
        JDialog dialog = new JDialog(frame, "History", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(createHistoryPanel());
        dialog.pack();
        dialog.setSize(new Dimension(640, 420));
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void showAboutDialog() {
        JDialog dialog = new JDialog(frame, "About", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(createAboutPanel());
        dialog.pack();
        dialog.setSize(new Dimension(560, 420));
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private JPanel createInfoPanel(String title, String formula, String description) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JTextArea infoArea = new JTextArea(6, 40);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoArea.setBackground(new Color(255, 255, 200));
        infoArea.setText(formula + "\n\n" + description);

        panel.add(new JScrollPane(infoArea), BorderLayout.CENTER);

        return panel;
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);

        JMenu viewMenu = new JMenu("View");
        showStatusItem = new JCheckBoxMenuItem("Show Status Bar", true);
        showStatusItem.addActionListener(e -> statusLabel.setVisible(showStatusItem.getState()));
        viewMenu.add(showStatusItem);

        exportItem = new JMenuItem("Export ke File");
        exportItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        exportItem.addActionListener(this);

        clearHistoryItem = new JMenuItem("Clear History");
        clearHistoryItem.addActionListener(e -> clearHistory());

        JMenu historyMenu = new JMenu("History");
        historyMenu.setMnemonic(KeyEvent.VK_H);
        openHistoryItem = new JMenuItem("Open History");
        openHistoryItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        openHistoryItem.addActionListener(this);
        historyMenu.add(openHistoryItem);
        historyMenu.addSeparator();
        historyMenu.add(clearHistoryItem);
        historyMenu.add(exportItem);

        JMenu helpMenu = new JMenu("Help");
        aboutItem = new JMenuItem("About");
        aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        aboutItem.addActionListener(this);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(historyMenu);
        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem runPolymorphismItem = new JMenuItem("Run Polymorphism Demo");
        runPolymorphismItem.addActionListener(e -> {
            // Run the polymorphism demo (integrated) so output is visible in terminal
            new Thread(() -> runPolymorphismDemo()).start();
        });
        JMenuItem runDemoItem = new JMenuItem("Run Parallel Demo");
        runDemoItem.addActionListener(e -> {
            // Run demo in background so UI stays responsive
            // Jalankan demo multithreading di thread terpisah agar GUI tidak freeze
            new Thread(() -> geometry.MultithreadingGeometryDemo.main(new String[0])).start();
        });
        toolsMenu.add(runPolymorphismItem);
        toolsMenu.add(runDemoItem);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApplication();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitItem) {
            closeApplication();
        } else if (e.getSource() == exportItem) {
            exportToFile();
        } else if (e.getSource() == openHistoryItem) {
            showHistoryDialog();
        } else if (e.getSource() == aboutItem) {
            showAboutDialog();
        }
    }

    private void calculateShape(BendaGeometri shape, String parameters, String label) {
        // Contoh polimorfisme: variabel bertipe BendaGeometri dapat merujuk ke objek
        // Elips, TabungElips, BolaElips, KerucutElips, KerucutTerpancung, Juring, Tembereng, CincinElips.
        // Pada runtime, pemanggilan shape.info() dan shape.hitungLuas() akan menggunakan implementasi subclass yang benar.
        // Ini mirip dengan contoh: Parent p1 = new Child1(); Parent p2 = new Child2();
        // Semua diproses melalui tipe super class yang sama.
        // If user chose to use shape-level threading, use the shape's async/future APIs
        if (cbUseShapeThreading != null && cbUseShapeThreading.isSelected()) {
            resultArea.setText("");
            statusLabel.setText(label + " sedang dihitung (shape-thread)");
            java.util.concurrent.Future<Double> future = shape.calculateWithFuture();
            new Thread(() -> {
                try {
                    Double res = future.get();
                    SwingUtilities.invokeLater(() -> {
                        resultArea.setText(shape.info());
                        statusLabel.setText(label + " selesai | result = " + String.format("%.4f", res));
                        // add to history
                        addToHistory(shape.getNama(), parameters, res, (shape instanceof VolumeCalculable) ? "volume" : "luas");
                        loadHistory();
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> showError("Error: " + e.getMessage()));
                }
            }).start();
            return;
        }

        if (calculator.isCalculating()) {
            showError("Perhitungan sedang berlangsung. Tunggu sampai selesai.");
            return;
        }

        calculator.calculateAsync(shape, parameters, new GeometryCalculator.GeometryCalculationCallback() {
            @Override
            public void onProgress(int progress) {
                progressBar.setVisible(true);
                progressBar.setValue(progress);
                statusLabel.setText(label + " sedang dihitung (" + progress + "%)");
            }

            @Override
            public void onComplete(double result, String resultType, String details) {
                progressBar.setVisible(false);
                progressBar.setValue(0);
                resultArea.setText(details);
                statusLabel.setText(label + " selesai | " + resultType + " = " + String.format("%.4f", result));
                loadHistory();
            }

            @Override
            public void onError(String error) {
                progressBar.setVisible(false);
                progressBar.setValue(0);
                showError(error);
            }
        });
    }

    private Elips resolveElipsBase(boolean usePanel, JTextField tfA, JTextField tfB) throws NumberFormatException {
        if (usePanel) {
            return getElipsFromPanel();
        } else {
            return getElipsFromFields(tfA, tfB);
        }
    }

    private Elips getElipsFromPanel() throws NumberFormatException {
        double a = Double.parseDouble(tfSumbuPanjang.getText().trim());
        double b = Double.parseDouble(tfSumbuPendek.getText().trim());
        if (a <= 0 || b <= 0) {
            throw new NumberFormatException("Sumbu Elips harus lebih dari 0");
        }
        return new Elips(a, b);
    }

    private Elips getElipsFromFields(JTextField tfA, JTextField tfB) throws NumberFormatException {
        double a = Double.parseDouble(tfA.getText().trim());
        double b = Double.parseDouble(tfB.getText().trim());
        if (a <= 0 || b <= 0) {
            throw new NumberFormatException("Nilai alas harus lebih dari 0");
        }
        return new Elips(a, b);
    }

    private void calculateElips() {
        try {
            double a = Double.parseDouble(tfSumbuPanjang.getText().trim());
            double b = Double.parseDouble(tfSumbuPendek.getText().trim());

            if (a <= 0 || b <= 0) {
                showError("Nilai sumbu harus lebih dari 0!");
                return;
            }

            Elips elips = new Elips(a, b);
            calculateShape(elips, String.format("a=%.2f, b=%.2f", a, b), "Elips");

        } catch (NumberFormatException ex) {
            showError("Input harus berupa angka yang valid!");
        }
    }

    private void calculateKerucutElips(Elips alas, double t) {
        try {
            if (alas.getSumbuPanjang() <= 0 || alas.getSumbuPendek() <= 0 || t <= 0) {
                showError("Semua nilai harus > 0!");
                return;
            }

            KerucutElips kerucut = new KerucutElips(alas, t);
            calculateShape(kerucut,
                    String.format("a=%.2f, b=%.2f, t=%.2f", alas.getSumbuPanjang(), alas.getSumbuPendek(), t),
                    "Kerucut Elips");
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private void calculateKerucutTerpancung(double a, double b, double t, double rAtas) {
        KerucutTerpancung kerucut = new KerucutTerpancung(a, b, t, rAtas);
        calculateShape(kerucut, String.format("a=%.2f, b=%.2f, t=%.2f, r=%.2f", a, b, t, rAtas), "Kerucut Terpancung");
    }

    private void calculateTabungElips(Elips alas, double t) {
        if (alas.getSumbuPanjang() <= 0 || alas.getSumbuPendek() <= 0 || t <= 0) {
            showError("Semua nilai harus > 0!");
            return;
        }
        TabungElips tabung = new TabungElips(alas, t);
        calculateShape(tabung,
                String.format("a=%.2f, b=%.2f, t=%.2f", alas.getSumbuPanjang(), alas.getSumbuPendek(), t),
                "Tabung Elips");
    }

    private void calculateBolaElips(double a, double b, double c) {
        BolaElips bola = new BolaElips(a, b, c);
        calculateShape(bola, String.format("a=%.2f, b=%.2f, c=%.2f", a, b, c), "Bola Elips");
    }

    private void calculateJuring(double r, double sudutRad, double t) {
        Juring juring = new Juring(r, sudutRad, t);
        calculateShape(juring, String.format("r=%.2f, sudut=%.2f°, t=%.2f", r, Math.toDegrees(sudutRad), t), "Juring");
    }

    private void calculateTembereng(double t, double R) {
        Tembereng tembereng = new Tembereng(t, R);
        calculateShape(tembereng, String.format("t=%.2f, R=%.2f", t, R), "Tembereng");
    }

    private void calculateCincin(double R, double r, double t) {
        CincinElips cincin = new CincinElips(R, r, t);
        calculateShape(cincin, String.format("R=%.2f, r=%.2f, t=%.2f", R, r, t), "Cincin Elips");
    }

    private void addToHistory(String shapeName, String parameters, double result, String resultType) {
        String record = String.format("[%tT] %s(%s) = %.4f %s",
                new java.util.Date(), shapeName, parameters, result, resultType);
        // Ensure history model exists before updating UI
        if (historyListModel == null) {
            historyListModel = new DefaultListModel<>();
            historyList = new JList<>(historyListModel);
        }
        historyListModel.add(0, record);
        calculator.addCalculationRecord(shapeName, parameters, result, resultType);
    }

    private void loadHistory() {
        // Lazily create history UI model if not yet created
        if (historyListModel == null) {
            historyListModel = new DefaultListModel<>();
            historyList = new JList<>(historyListModel);
        }
        historyListModel.clear();
        java.util.List<GeometryCalculator.CalculationRecord> records = calculator.getHistory();
        if (records.isEmpty()) {
            historyListModel.addElement("=== HISTORY KOSONG ===");
            historyListModel.addElement("Lakukan perhitungan untuk menambah history");
            return;
        }
        historyListModel.addElement("=== HISTORY PERHITUNGAN ===");
        for (GeometryCalculator.CalculationRecord record : records) {
            historyListModel.addElement(record.toString());
        }
    }

    private void clearHistory() {
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Yakin ingin menghapus semua history?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            historyListModel.clear();
            calculator.clearHistory();
            statusLabel.setText("History telah dihapus");

            // Optional: Tambahkan pesan bahwa history kosong
            historyListModel.addElement("=== HISTORY KOSONG ===");
            historyListModel.addElement("Lakukan perhitungan untuk menambah history");
        }
    }

    private void exportToFile() {
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        JFileChooser fileChooser = new JFileChooser("output");
        fileChooser.setSelectedFile(new File("output/geometry_history.txt"));

        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();

            calculator.saveHistoryToFile(filename);
            JOptionPane.showMessageDialog(frame,
                    "History berhasil diekspor ke:\n" + filename,
                    "Export Sukses", JOptionPane.INFORMATION_MESSAGE);
            statusLabel.setText("History diekspor ke: " + filename);
        }
    }

    // Redirect System.out and System.err to the GUI resultArea so thread logs are visible
    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            private final StringBuilder sb = new StringBuilder();

            @Override
            public void write(int b) throws IOException {
                if (b == '\r') return;
                sb.append((char) b);
                if (b == '\n') {
                    flush();
                }
            }

            @Override
            public void flush() throws IOException {
                final String text = sb.toString();
                SwingUtilities.invokeLater(() -> resultArea.append(text));
                sb.setLength(0);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("Error: " + message);
    }

    private void closeApplication() {
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Yakin ingin keluar dari aplikasi?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            frame.dispose();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new GeometryGUI());
    }
}