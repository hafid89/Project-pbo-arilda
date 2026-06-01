package ui;

import geometry.*;
import calculator.GeometryCalculator;
import exceptions.GeometryException;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

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
    private JCheckBox cbUseElipsBaseForJuring2D, cbUseElipsBaseForTembereng2D, cbUseElipsBaseForCincin2D;
    private JCheckBox cbUseBolaBaseForJuring3D, cbUseBolaBaseForTembereng3D;
    private JCheckBox cbUseShapeThreading;

    private JPanel demoPanel;
    private JLabel demoShapeLabel;
    private JLabel demoStepLabel;
    private JLabel demoStatusLabel;
    private JProgressBar demoProgressBar;
    private JTextArea demoLogArea;
    private JTextArea demoResultArea;
    private ShapePreviewPanel shapePreviewPanel;

    private GeometryCalculator calculator;

    private JMenuItem exitItem, exportItem, aboutItem, openHistoryItem, clearHistoryItem;
    private JCheckBoxMenuItem showStatusItem;

    private DefaultListModel<String> historyListModel;
    private JList<String> historyList;

    public GeometryGUI() {
        calculator = new GeometryCalculator();
        initComponents();
        setupMenu();
        frame.setVisible(true);
    }

    private void initComponents() {
        frame = new JFrame("Aplikasi Perhitungan Benda Geometri");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(1200, 850);
        frame.setMinimumSize(new Dimension(1000, 750));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Set UI defaults
        UIManager.put("Button.background", new Color(56, 130, 242));
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 12));
        UIManager.put("TabbedPane.background", new Color(245, 248, 255));
        UIManager.put("TabbedPane.selected", new Color(220, 235, 255));
        UIManager.put("Panel.background", new Color(248, 251, 255));
        UIManager.put("TextField.background", new Color(255, 255, 255));
        UIManager.put("TextArea.background", new Color(250, 250, 255));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(26, 115, 232));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JLabel titleLabel = new JLabel("Geometry Calculator");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        JLabel subtitle = new JLabel("Aplikasi Perhitungan Benda Geometri 2D & 3D dengan dasar Elips");
        subtitle.setForeground(new Color(220, 235, 255));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabbedPane.setBackground(new Color(245, 248, 255));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        tabbedPane.addTab("Elips (2D)", createScrollTab(create2DPanel()));
        tabbedPane.addTab("Juring (2D)", createScrollTab(createJuring2DPanel()));
        tabbedPane.addTab("Tembereng (2D)", createScrollTab(createTembereng2DPanel()));
        tabbedPane.addTab("Cincin Elips (2D)", createScrollTab(createCincin2DPanel()));
        tabbedPane.addTab("Kerucut Elips", createScrollTab(create3DLimasPanel()));
        tabbedPane.addTab("Kerucut Terpancung", createScrollTab(createKerucutTerpancungPanel()));
        tabbedPane.addTab("Tabung Elips", createScrollTab(createTabungElipsPanel()));
        tabbedPane.addTab("Bola Elips", createScrollTab(createBolaElipsPanel()));
        tabbedPane.addTab("Juring (3D)", createScrollTab(createJuringPanel()));
        tabbedPane.addTab("Tembereng (3D)", createScrollTab(createTemberengPanel()));
        tabbedPane.addTab("Cincin Elips (3D)", createScrollTab(createCincinPanel()));
        tabbedPane.addTab("Demo Multithreading", createScrollTab(createDemoPanel()));

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        statusLabel = new JLabel("Siap melakukan perhitungan");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(progressBar, BorderLayout.CENTER);
        
        cbUseShapeThreading = new JCheckBox("Use shape-level threading", false);
        bottomPanel.add(cbUseShapeThreading, BorderLayout.EAST);

        // Gunakan `demoResultArea` sebagai area hasil global sehingga
        // tidak menduplikasi panel hasil. `createDemoPanel()` sudah membuat
        // `demoResultArea` sehingga cukup mengaitkannya ke `resultArea`.
        resultArea = demoResultArea;
        redirectSystemStreams();

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Helper method untuk membuat panel dengan input di kiri dan rumus di kanan
     */
    private JPanel createSplitPanel(JPanel inputPanel, String formulaTitle, String formula, String description) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.BOTH;
        
        // Input panel di kiri (70%)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.weighty = 1.0;
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(100, 150, 255)), "Input Parameter"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        mainPanel.add(inputPanel, gbc);
        
        // Info panel di kanan (30%)
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        JPanel infoPanel = createInfoPanel(formulaTitle, formula, description);
        infoPanel.setPreferredSize(new Dimension(300, 0));
        mainPanel.add(infoPanel, gbc);
        
        return mainPanel;
    }

    private Component createScrollTab(JComponent content) {
        JScrollPane scrollPane = new JScrollPane(content,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel create2DPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Baris 0 - Sumbu Panjang
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel labelA = new JLabel("Sumbu Panjang (a):");
        labelA.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelA, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        tfSumbuPanjang = new JTextField(15);
        tfSumbuPanjang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfSumbuPanjang, gbc);

        // Baris 1 - Sumbu Pendek
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel labelB = new JLabel("Sumbu Pendek (b):");
        labelB.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelB, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        tfSumbuPendek = new JTextField(15);
        tfSumbuPendek.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfSumbuPendek, gbc);

        // Baris 2 - Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnHitung = new JButton("Hitung");
        JButton btnReset = new JButton("Reset");
        
        btnHitung.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);
        btnHitung.setPreferredSize(new Dimension(120, 35));
        
        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnReset.setBackground(new Color(220, 53, 69));
        btnReset.setForeground(Color.BLACK);
        btnReset.setFocusPainted(false);
        btnReset.setPreferredSize(new Dimension(120, 35));

        btnHitung.addActionListener(e -> calculateElips());
        btnReset.addActionListener(e -> {
            tfSumbuPanjang.setText("");
            tfSumbuPendek.setText("");
        });

        buttonPanel.add(btnHitung);
        buttonPanel.add(btnReset);
        inputPanel.add(buttonPanel, gbc);

        String formula = "Rumus:\n• Luas = π × a × b\n• Keliling ≈ π × [3(a+b) - √((3a+b)(a+3b))]";
        String description = "Keterangan:\na = sumbu panjang\nb = sumbu pendek";
        
        return createSplitPanel(inputPanel, "Elips", formula, description);
    }

    private JPanel create3DLimasPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Baris 0
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        JLabel labelA = new JLabel("Sumbu Panjang Alas (a):");
        labelA.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelA, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        JTextField tfAlasPanjang = new JTextField(15);
        tfAlasPanjang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfAlasPanjang, gbc);

        // Baris 1
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel labelB = new JLabel("Sumbu Pendek Alas (b):");
        labelB.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelB, gbc);
        
        gbc.gridx = 1;
        JTextField tfAlasPendek = new JTextField(15);
        tfAlasPendek.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfAlasPendek, gbc);

        // Baris 2
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel labelT = new JLabel("Tinggi Kerucut (t):");
        labelT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelT, gbc);
        
        gbc.gridx = 1;
        tfTinggi = new JTextField(15);
        tfTinggi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfTinggi, gbc);

        // Baris 3 - Checkbox
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        cbUseElipsBaseForKerucut = new JCheckBox("Gunakan data Elips dari panel Elips");
        cbUseElipsBaseForKerucut.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(cbUseElipsBaseForKerucut, gbc);

        // Baris 4 & 5 - Override fields
        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel overrideALabel = new JLabel("Override Alas (a):");
        overrideALabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(overrideALabel, gbc);
        gbc.gridx = 1;
        JTextField tfOverrideA = new JTextField(15);
        tfOverrideA.setEnabled(false);
        tfOverrideA.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfOverrideA, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel overrideBLabel = new JLabel("Override Alas (b):");
        overrideBLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(overrideBLabel, gbc);
        gbc.gridx = 1;
        JTextField tfOverrideB = new JTextField(15);
        tfOverrideB.setEnabled(false);
        tfOverrideB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfOverrideB, gbc);

        cbUseElipsBaseForKerucut.addActionListener(e -> {
            boolean usePanel = cbUseElipsBaseForKerucut.isSelected();
            tfOverrideA.setEnabled(!usePanel);
            tfOverrideB.setEnabled(!usePanel);
            if (usePanel) {
                tfOverrideA.setText("");
                tfOverrideB.setText("");
            }
        });

        // Baris 6 - Button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnHitung = new JButton("Hitung");
        btnHitung.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);
        btnHitung.setPreferredSize(new Dimension(120, 35));
        
        btnHitung.addActionListener(e -> {
            try {
                double t = Double.parseDouble(tfTinggi.getText().trim());
                Elips alas = resolveElipsBase(cbUseElipsBaseForKerucut.isSelected(), tfOverrideA, tfOverrideB);
                calculateKerucutElips(alas, t);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka yang valid dan lebih dari 0!");
            }
        });
        inputPanel.add(btnHitung, gbc);

        String formula = "Rumus:\n• Volume = 1/3 × Luas Alas × t\n• Luas Alas = π × a × b\n• Luas Selimut ≈ π × (a+b) × l";
        String description = "Keterangan:\na = sumbu panjang alas\nb = sumbu pendek alas\nt = tinggi kerucut";
        
        return createSplitPanel(inputPanel, "Kerucut Elips", formula, description);
    }

    private JPanel createKerucutTerpancungPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        JLabel labelA = new JLabel("Sumbu Panjang Alas (a):");
        labelA.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelA, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        JTextField tfAlasPanjang = new JTextField(15);
        tfAlasPanjang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfAlasPanjang, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel labelB = new JLabel("Sumbu Pendek Alas (b):");
        labelB.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelB, gbc);
        gbc.gridx = 1;
        JTextField tfAlasPendek = new JTextField(15);
        tfAlasPendek.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfAlasPendek, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel labelT = new JLabel("Tinggi (t):");
        labelT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelT, gbc);
        gbc.gridx = 1;
        tfTinggi = new JTextField(15);
        tfTinggi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfTinggi, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel labelR = new JLabel("Jari-jari Atas (r):");
        labelR.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelR, gbc);
        gbc.gridx = 1;
        tfJariJariAtas = new JTextField(15);
        tfJariJariAtas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfJariJariAtas, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        cbUseElipsBaseForKerucutTerp = new JCheckBox("Gunakan data Elips dari panel Elips");
        cbUseElipsBaseForKerucutTerp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(cbUseElipsBaseForKerucutTerp, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 5;
        gbc.gridx = 0;
        JLabel overrideALabel = new JLabel("Override Sumbu Panjang (a):");
        overrideALabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(overrideALabel, gbc);
        gbc.gridx = 1;
        JTextField tfOverrideA = new JTextField(15);
        tfOverrideA.setEnabled(false);
        tfOverrideA.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfOverrideA, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel overrideBLabel = new JLabel("Override Sumbu Pendek (b):");
        overrideBLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(overrideBLabel, gbc);
        gbc.gridx = 1;
        JTextField tfOverrideB = new JTextField(15);
        tfOverrideB.setEnabled(false);
        tfOverrideB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfOverrideB, gbc);

        cbUseElipsBaseForKerucutTerp.addActionListener(e -> {
            boolean usePanel = cbUseElipsBaseForKerucutTerp.isSelected();
            tfOverrideA.setEnabled(!usePanel);
            tfOverrideB.setEnabled(!usePanel);
            if (usePanel) {
                tfOverrideA.setText("");
                tfOverrideB.setText("");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnHitung = new JButton("Hitung");
        btnHitung.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);
        btnHitung.setPreferredSize(new Dimension(120, 35));
        
        btnHitung.addActionListener(e -> {
            try {
                double t = Double.parseDouble(tfTinggi.getText().trim());
                double r = Double.parseDouble(tfJariJariAtas.getText().trim());
                Elips alas = resolveElipsBase(cbUseElipsBaseForKerucutTerp.isSelected(), tfOverrideA, tfOverrideB);
                calculateKerucutTerpancung(alas.getSumbuPanjang(), alas.getSumbuPendek(), t, r);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka yang valid dan lebih dari 0!");
            }
        });
        inputPanel.add(btnHitung, gbc);

        String formula = "Rumus:\n• Volume = 1/3 × π × t × (a² + ab + b²)\n• Luas Alas = π × a × b\n• Luas Permukaan = π × (a+b) × s + π × a × b + π × r²";
        String description = "Keterangan:\na, b = sumbu alas\nt = tinggi\nr = jari-jari atas";
        
        return createSplitPanel(inputPanel, "Kerucut Terpancung", formula, description);
    }

    private JPanel createTabungElipsPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        JLabel labelA = new JLabel("Sumbu Panjang Alas (a):");
        labelA.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelA, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        JTextField tfA = new JTextField(15);
        tfA.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfA, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel labelB = new JLabel("Sumbu Pendek Alas (b):");
        labelB.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelB, gbc);
        gbc.gridx = 1;
        JTextField tfB = new JTextField(15);
        tfB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel labelT = new JLabel("Tinggi Tabung (t):");
        labelT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelT, gbc);
        gbc.gridx = 1;
        JTextField tfT = new JTextField(15);
        tfT.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfT, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        cbUseElipsBaseForTabung = new JCheckBox("Gunakan data Elips dari panel Elips");
        cbUseElipsBaseForTabung.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(cbUseElipsBaseForTabung, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel overrideALabel = new JLabel("Override Alas (a):");
        overrideALabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(overrideALabel, gbc);
        gbc.gridx = 1;
        JTextField tfOverrideA = new JTextField(15);
        tfOverrideA.setEnabled(false);
        tfOverrideA.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfOverrideA, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel overrideBLabel = new JLabel("Override Alas (b):");
        overrideBLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(overrideBLabel, gbc);
        gbc.gridx = 1;
        JTextField tfOverrideB = new JTextField(15);
        tfOverrideB.setEnabled(false);
        tfOverrideB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfOverrideB, gbc);

        cbUseElipsBaseForTabung.addActionListener(e -> {
            boolean usePanel = cbUseElipsBaseForTabung.isSelected();
            tfOverrideA.setEnabled(!usePanel);
            tfOverrideB.setEnabled(!usePanel);
            if (usePanel) {
                tfOverrideA.setText("");
                tfOverrideB.setText("");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnHitung = new JButton("Hitung");
        btnHitung.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);
        btnHitung.setPreferredSize(new Dimension(120, 35));
        
        btnHitung.addActionListener(e -> {
            try {
                double t = Double.parseDouble(tfT.getText().trim());
                Elips alas = resolveElipsBase(cbUseElipsBaseForTabung.isSelected(), tfOverrideA, tfOverrideB);
                calculateTabungElips(alas, t);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka yang valid dan lebih dari 0!");
            }
        });
        inputPanel.add(btnHitung, gbc);

        String formula = "Rumus:\n• Volume = Luas Alas × t\n• Luas Alas = π × a × b\n• Luas Selimut = 2 × π × √((a²+b²)/2) × t";
        String description = "Keterangan:\na, b = sumbu alas\nt = tinggi tabung";
        
        return createSplitPanel(inputPanel, "Tabung Elips", formula, description);
    }

    private JPanel createBolaElipsPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        JLabel labelX = new JLabel("Radius Sumbu X (a):");
        labelX.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelX, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        tfSumbuX = new JTextField(15);
        tfSumbuX.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfSumbuX, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel labelY = new JLabel("Radius Sumbu Y (b):");
        labelY.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelY, gbc);
        gbc.gridx = 1;
        tfSumbuY = new JTextField(15);
        tfSumbuY.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfSumbuY, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel labelZ = new JLabel("Radius Sumbu Z (c):");
        labelZ.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelZ, gbc);
        gbc.gridx = 1;
        tfSumbuZ = new JTextField(15);
        tfSumbuZ.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfSumbuZ, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnHitung = new JButton("Hitung");
        btnHitung.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);
        btnHitung.setPreferredSize(new Dimension(120, 35));
        
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
        inputPanel.add(btnHitung, gbc);

        String formula = "Rumus:\n• Volume = 4/3 × π × a × b × c\n• Luas Permukaan ≈ 4π × ((a^p b^p + a^p c^p + b^p c^p)/3)^(1/p)";
        String description = "Keterangan:\na, b, c = semiaxis bola elips";
        
        return createSplitPanel(inputPanel, "Bola Elips", formula, description);
    }

    private JPanel createJuringPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        JLabel labelR = new JLabel("Jari-jari (r):");
        labelR.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelR, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        JTextField tfR = new JTextField(15);
        tfR.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfR, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        cbUseBolaBaseForJuring3D = new JCheckBox("Gunakan data Bola Elips dari panel Bola");
        cbUseBolaBaseForJuring3D.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(cbUseBolaBaseForJuring3D, gbc);

        cbUseBolaBaseForJuring3D.addActionListener(e -> {
            boolean usePanel = cbUseBolaBaseForJuring3D.isSelected();
            tfR.setEnabled(!usePanel);
            if (usePanel) {
                tfR.setText("");
            }
        });

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel labelSudut = new JLabel("Sudut (derajat):");
        labelSudut.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelSudut, gbc);
        gbc.gridx = 1;
        tfSudut = new JTextField(15);
        tfSudut.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfSudut, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel labelT = new JLabel("Tinggi (t):");
        labelT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelT, gbc);
        gbc.gridx = 1;
        JTextField tfT = new JTextField(15);
        tfT.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfT, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnHitung = new JButton("Hitung");
        btnHitung.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);
        btnHitung.setPreferredSize(new Dimension(120, 35));
        
        btnHitung.addActionListener(e -> {
            try {
                double r;
                if (cbUseBolaBaseForJuring3D.isSelected()) {
                    BolaElips bola = getBolaFromPanel();
                    r = bola.getSumbuPanjang();
                } else {
                    r = Double.parseDouble(tfR.getText());
                }
                double sudut = Double.parseDouble(tfSudut.getText());
                double t = Double.parseDouble(tfT.getText());
                calculateJuring(r, Math.toRadians(sudut), t);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka!");
            }
        });
        inputPanel.add(btnHitung, gbc);

        String formula = "Rumus:\n• Volume = 1/2 × r² × sudut × t\n• Luas Permukaan = 2×Luas Alas + selimut\n• Keliling Alas = r × sudut + 2r";
        String description = "Keterangan:\nr = jari-jari\nt = tinggi\nsudut dalam radian";
        
        return createSplitPanel(inputPanel, "Juring (3D)", formula, description);
    }

    private JPanel createTemberengPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        cbUseBolaBaseForTembereng3D = new JCheckBox("Gunakan data Bola Elips dari panel Bola");
        cbUseBolaBaseForTembereng3D.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(cbUseBolaBaseForTembereng3D, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0.35;
        JLabel labelT = new JLabel("Tinggi Tembereng (t):");
        labelT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelT, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        JTextField tfT = new JTextField(15);
        tfT.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfT, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel labelR = new JLabel("Radius Bola (R):");
        labelR.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelR, gbc);
        gbc.gridx = 1;
        tfRadiusBola = new JTextField(15);
        tfRadiusBola.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfRadiusBola.setEnabled(!cbUseBolaBaseForTembereng3D.isSelected());
        inputPanel.add(tfRadiusBola, gbc);

        cbUseBolaBaseForTembereng3D.addActionListener(e -> {
            boolean usePanel = cbUseBolaBaseForTembereng3D.isSelected();
            tfRadiusBola.setEnabled(!usePanel);
            if (usePanel) {
                tfRadiusBola.setText("");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnHitung = new JButton("Hitung");
        btnHitung.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);
        btnHitung.setPreferredSize(new Dimension(120, 35));
        
        btnHitung.addActionListener(e -> {
            try {
                double t = Double.parseDouble(tfT.getText());
                double R;
                if (cbUseBolaBaseForTembereng3D.isSelected()) {
                    BolaElips bola = getBolaFromPanel();
                    R = (bola.getSumbuPanjang() + bola.getSumbuPendek() + bola.getSumbuZ()) / 3.0;
                } else {
                    R = Double.parseDouble(tfRadiusBola.getText());
                }
                calculateTembereng(t, R);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka!");
            }
        });
        inputPanel.add(btnHitung, gbc);

        String formula = "Rumus:\n• Volume = π × t² × (R - t/3)\n• Luas Permukaan = 2πR t + πR²";
        String description = "Keterangan:\nt = tinggi tembereng\nR = radius bola";
        
        return createSplitPanel(inputPanel, "Tembereng (3D)", formula, description);
    }

    private JPanel createCincinPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        JLabel labelR = new JLabel("Radius Utama (R):");
        labelR.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelR, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        tfJariJariLuar = new JTextField(15);
        tfJariJariLuar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfJariJariLuar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel labelA = new JLabel("Semi Mayor Elips (a):");
        labelA.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelA, gbc);
        gbc.gridx = 1;
        tfJariJariDalam = new JTextField(15);
        tfJariJariDalam.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfJariJariDalam, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel labelB = new JLabel("Semi Minor Elips (b):");
        labelB.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelB, gbc);
        gbc.gridx = 1;
        JTextField tfB = new JTextField(15);
        tfB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnHitung = new JButton("Hitung");
        btnHitung.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);
        btnHitung.setPreferredSize(new Dimension(120, 35));
        
        btnHitung.addActionListener(e -> {
            try {
                double R = Double.parseDouble(tfJariJariLuar.getText());
                double a = Double.parseDouble(tfJariJariDalam.getText());
                double b = Double.parseDouble(tfB.getText());
                calculateCincin(R, a, b);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka!");
            }
        });
        inputPanel.add(btnHitung, gbc);

        String formula = "Rumus:\n• Volume = 2π² × R × a × b\n• Luas Permukaan ≈ 4π² × R × √((a²+b²)/2)";
        String description = "Keterangan:\nR = radius utama\na = semi mayor elips\nb = semi minor elips";
        
        return createSplitPanel(inputPanel, "Cincin Elips (3D)", formula, description);
    }

    // Methods untuk 2D panels dengan layout yang sama
    private JPanel createJuring2DPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        JLabel labelA = new JLabel("Sumbu Panjang (a):");
        labelA.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelA, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        JTextField tfA = new JTextField(15);
        tfA.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfA, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel labelB = new JLabel("Sumbu Pendek (b):");
        labelB.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelB, gbc);
        gbc.gridx = 1;
        JTextField tfB = new JTextField(15);
        tfB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel labelTheta = new JLabel("Sudut Juring (derajat):");
        labelTheta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelTheta, gbc);
        gbc.gridx = 1;
        JTextField tfTheta = new JTextField(15);
        tfTheta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfTheta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        cbUseElipsBaseForJuring2D = new JCheckBox("Gunakan data Elips dari panel Elips");
        cbUseElipsBaseForJuring2D.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(cbUseElipsBaseForJuring2D, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel overrideALabel = new JLabel("Override Sumbu Panjang (a):");
        overrideALabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(overrideALabel, gbc);
        gbc.gridx = 1;
        JTextField tfAOverride = new JTextField(15);
        tfAOverride.setEnabled(false);
        tfAOverride.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfAOverride, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel overrideBLabel = new JLabel("Override Sumbu Pendek (b):");
        overrideBLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(overrideBLabel, gbc);
        gbc.gridx = 1;
        JTextField tfBOverride = new JTextField(15);
        tfBOverride.setEnabled(false);
        tfBOverride.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfBOverride, gbc);

        cbUseElipsBaseForJuring2D.addActionListener(e -> {
            boolean usePanel = cbUseElipsBaseForJuring2D.isSelected();
            tfAOverride.setEnabled(!usePanel);
            tfBOverride.setEnabled(!usePanel);
            if (usePanel) {
                tfAOverride.setText("");
                tfBOverride.setText("");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnHitung = new JButton("Hitung");
        btnHitung.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);
        btnHitung.setPreferredSize(new Dimension(120, 35));
        
        btnHitung.addActionListener(e -> {
            try {
                Elips basis = resolveElipsBase(cbUseElipsBaseForJuring2D.isSelected(), tfAOverride, tfBOverride);
                double a = basis.getSumbuPanjang();
                double b = basis.getSumbuPendek();
                double sudut = Double.parseDouble(tfTheta.getText().trim());
                calculateJuring2D(a, b, sudut);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka yang valid!");
            }
        });
        inputPanel.add(btnHitung, gbc);

        String formula = "Rumus:\n• Luas = (sudut/360) × π × a × b\n• Keliling ≈ 2r + busur";
        String description = "Keterangan:\na = sumbu panjang\nb = sumbu pendek\nsudut dalam derajat";
        
        return createSplitPanel(inputPanel, "Juring (2D)", formula, description);
    }

    private JPanel createTembereng2DPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        JLabel labelA = new JLabel("Sumbu Panjang (a):");
        labelA.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelA, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        JTextField tfA = new JTextField(15);
        tfA.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfA, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel labelB = new JLabel("Sumbu Pendek (b):");
        labelB.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelB, gbc);
        gbc.gridx = 1;
        JTextField tfB = new JTextField(15);
        tfB.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel labelTheta = new JLabel("Sudut Tembereng (derajat):");
        labelTheta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelTheta, gbc);
        gbc.gridx = 1;
        JTextField tfTheta = new JTextField(15);
        tfTheta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfTheta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        cbUseElipsBaseForTembereng2D = new JCheckBox("Gunakan data Elips dari panel Elips");
        cbUseElipsBaseForTembereng2D.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(cbUseElipsBaseForTembereng2D, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel overrideALabel = new JLabel("Override Sumbu Panjang (a):");
        overrideALabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(overrideALabel, gbc);
        gbc.gridx = 1;
        JTextField tfAOverride = new JTextField(15);
        tfAOverride.setEnabled(false);
        tfAOverride.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfAOverride, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel overrideBLabel = new JLabel("Override Sumbu Pendek (b):");
        overrideBLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(overrideBLabel, gbc);
        gbc.gridx = 1;
        JTextField tfBOverride = new JTextField(15);
        tfBOverride.setEnabled(false);
        tfBOverride.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfBOverride, gbc);

        cbUseElipsBaseForTembereng2D.addActionListener(e -> {
            boolean usePanel = cbUseElipsBaseForTembereng2D.isSelected();
            tfAOverride.setEnabled(!usePanel);
            tfBOverride.setEnabled(!usePanel);
            if (usePanel) {
                tfAOverride.setText("");
                tfBOverride.setText("");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnHitung = new JButton("Hitung");
        btnHitung.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);
        btnHitung.setPreferredSize(new Dimension(120, 35));
        
        btnHitung.addActionListener(e -> {
            try {
                Elips basis = resolveElipsBase(cbUseElipsBaseForTembereng2D.isSelected(), tfAOverride, tfBOverride);
                double a = basis.getSumbuPanjang();
                double b = basis.getSumbuPendek();
                double sudut = Double.parseDouble(tfTheta.getText().trim());
                calculateTembereng2D(a, b, sudut);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka yang valid!");
            }
        });
        inputPanel.add(btnHitung, gbc);

        String formula = "Rumus:\n• Luas = Luas juring - Luas segitiga\n• Keliling = busur + chord";
        String description = "Keterangan:\na = sumbu panjang\nb = sumbu pendek\nsudut dalam derajat";
        
        return createSplitPanel(inputPanel, "Tembereng (2D)", formula, description);
    }

    private JPanel createCincin2DPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        JLabel labelA1 = new JLabel("Sumbu Panjang Luar (a1):");
        labelA1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelA1, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        JTextField tfA1 = new JTextField(15);
        tfA1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfA1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel labelB1 = new JLabel("Sumbu Pendek Luar (b1):");
        labelB1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelB1, gbc);
        gbc.gridx = 1;
        JTextField tfB1 = new JTextField(15);
        tfB1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfB1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel labelA2 = new JLabel("Sumbu Panjang Dalam (a2):");
        labelA2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelA2, gbc);
        gbc.gridx = 1;
        JTextField tfA2 = new JTextField(15);
        tfA2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfA2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel labelB2 = new JLabel("Sumbu Pendek Dalam (b2):");
        labelB2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputPanel.add(labelB2, gbc);
        gbc.gridx = 1;
        JTextField tfB2 = new JTextField(15);
        tfB2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfB2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        cbUseElipsBaseForCincin2D = new JCheckBox("Gunakan data Elips luar dari panel Elips");
        cbUseElipsBaseForCincin2D.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(cbUseElipsBaseForCincin2D, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 5;
        gbc.gridx = 0;
        JLabel overrideA1Label = new JLabel("Override Sumbu Panjang Luar (a1):");
        overrideA1Label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(overrideA1Label, gbc);
        gbc.gridx = 1;
        JTextField tfA1Override = new JTextField(15);
        tfA1Override.setEnabled(false);
        tfA1Override.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfA1Override, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel overrideB1Label = new JLabel("Override Sumbu Pendek Luar (b1):");
        overrideB1Label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(overrideB1Label, gbc);
        gbc.gridx = 1;
        JTextField tfB1Override = new JTextField(15);
        tfB1Override.setEnabled(false);
        tfB1Override.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tfB1Override, gbc);

        cbUseElipsBaseForCincin2D.addActionListener(e -> {
            boolean usePanel = cbUseElipsBaseForCincin2D.isSelected();
            tfA1Override.setEnabled(!usePanel);
            tfB1Override.setEnabled(!usePanel);
            if (usePanel) {
                tfA1Override.setText("");
                tfB1Override.setText("");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnHitung = new JButton("Hitung");
        btnHitung.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);
        btnHitung.setPreferredSize(new Dimension(120, 35));
        
        btnHitung.addActionListener(e -> {
            try {
                double a1;
                double b1;
                if (cbUseElipsBaseForCincin2D.isSelected()) {
                    Elips basis = getElipsFromPanel();
                    a1 = basis.getSumbuPanjang();
                    b1 = basis.getSumbuPendek();
                } else {
                    a1 = Double.parseDouble(tfA1Override.getText().trim());
                    b1 = Double.parseDouble(tfB1Override.getText().trim());
                }
                double a2 = Double.parseDouble(tfA2.getText().trim());
                double b2 = Double.parseDouble(tfB2.getText().trim());
                calculateCincin2D(a1, b1, a2, b2);
            } catch (NumberFormatException ex) {
                showError("Input harus berupa angka yang valid!");
            }
        });
        inputPanel.add(btnHitung, gbc);

        String formula = "Rumus:\n• Luas = Luas elips luar - Luas elips dalam\n• Keliling ≈ keliling luar + keliling dalam";
        String description = "Keterangan:\na1, b1 = elips luar\na2, b2 = elips dalam";
        
        return createSplitPanel(inputPanel, "Cincin Elips (2D)", formula, description);
    }

    private JPanel createDemoPanel() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        main.setPreferredSize(new Dimension(940, 900));
        main.setBackground(new Color(245, 248, 255));

        // === TOP PANEL ===
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // === LEFT PANEL (stacked: info + results) ===
        JPanel leftTopPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        leftTopPanel.setOpaque(false);

        // Info Panel (shape, progress, status)
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(100, 150, 255)), "Demo Multithreading"));

        demoShapeLabel = new JLabel("Shape: -");
        demoShapeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        demoStepLabel = new JLabel("Progress: -");
        demoStepLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        demoStatusLabel = new JLabel("Status: Tunggu mulai demo");
        demoStatusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        infoPanel.add(demoShapeLabel);
        infoPanel.add(demoStepLabel);
        infoPanel.add(demoStatusLabel);
        leftTopPanel.add(infoPanel);

        // Results Panel (hasil perhitungan)
        demoResultArea = new JTextArea(6, 40);
        demoResultArea.setEditable(false);
        demoResultArea.setLineWrap(true);
        demoResultArea.setWrapStyleWord(true);
        demoResultArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        demoResultArea.setBackground(new Color(250, 250, 255));
        JScrollPane resultScroll = new JScrollPane(demoResultArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resultScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(100, 150, 255)), "Hasil Perhitungan"));
        leftTopPanel.add(resultScroll);

        topPanel.add(leftTopPanel);

        // === RIGHT PANEL (preview shape) ===
        shapePreviewPanel = new ShapePreviewPanel();
        shapePreviewPanel.setPreferredSize(new Dimension(320, 220));
        shapePreviewPanel.setMinimumSize(new Dimension(280, 220));
        shapePreviewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(100, 150, 255)), "Preview Shape"));
        topPanel.add(shapePreviewPanel);

        topPanel.setPreferredSize(new Dimension(0, 360));
        main.add(topPanel, BorderLayout.NORTH);

        // === PROGRESS BAR ===
        demoProgressBar = new JProgressBar(0, 100);
        demoProgressBar.setStringPainted(true);
        demoProgressBar.setPreferredSize(new Dimension(0, 24));
        demoProgressBar.setVisible(false);
        main.add(demoProgressBar, BorderLayout.SOUTH);

        // === CENTER PANEL (log demo only) ===
        demoLogArea = new JTextArea(30, 40);
        demoLogArea.setEditable(false);
        demoLogArea.setLineWrap(true);
        demoLogArea.setWrapStyleWord(true);
        demoLogArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        demoLogArea.setBackground(new Color(250, 250, 255));
        JScrollPane demoScroll = new JScrollPane(demoLogArea,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        demoScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(100, 150, 255)), "Log Demo"));
        main.add(demoScroll, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionPanel.setOpaque(false);
        JButton btnStartDemo = new JButton("Mulai Demo Multithreading");
        btnStartDemo.setBackground(new Color(56, 130, 242));
        btnStartDemo.setForeground(Color.WHITE);
        btnStartDemo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnStartDemo.setPreferredSize(new Dimension(220, 38));
        btnStartDemo.addActionListener(e -> {
            btnStartDemo.setEnabled(false);
            runMultithreadingGuiDemo(() -> btnStartDemo.setEnabled(true));
        });
        actionPanel.add(btnStartDemo);

        main.add(actionPanel, BorderLayout.PAGE_END);
        return main;
    }

    private void runMultithreadingGuiDemo(Runnable onComplete) {
        demoProgressBar.setValue(0);
        demoProgressBar.setVisible(true);
        demoLogArea.setText("");
        demoResultArea.setText("");
        demoShapeLabel.setText("Shape: -");
        demoStepLabel.setText("Progress: -");
        demoStatusLabel.setText("Status: Menyiapkan demo...");

        List<BendaGeometri> demoShapes = new ArrayList<>();
        demoShapes.add(new Elips(5.0, 3.0));
        try {
            demoShapes.add(new Juring2Dimensi(5.0, 3.0, 45.0));
            demoShapes.add(new Tembereng2Dimensi(5.0, 3.0, 60.0));
            demoShapes.add(new CincinElips2Dimensi(8.0, 5.0, 4.0, 2.5));
        } catch (GeometryException ex) {
            // Gagal membuat shape 2D, lanjutkan dengan shape yang ada
        }
        demoShapes.add(new KerucutElips(4.0, 2.0, 6.0));
        demoShapes.add(new KerucutTerpancung(4.0, 2.0, 5.0, 1.0));
        demoShapes.add(new TabungElips(4.0, 2.0, 5.0));
        demoShapes.add(new BolaElips(3.0, 2.0, 2.0));
        demoShapes.add(new Juring(3.0, Math.PI / 3.0, 4.0));
        demoShapes.add(new Tembereng(1.0, 2.0));
        demoShapes.add(new CincinElips(4.0, 1.4, 1.0));

        java.util.Map<String, Integer> shapeProgress = new java.util.HashMap<>();
        demoShapes.forEach(shape -> {
            shape.setTotalIterations(10);
            shape.setIterationDelayMs(500);
            shapeProgress.put(shape.getNama(), 0);
            shape.setProgressListener((shape1, currentIteration, totalIterations, message) -> {
                SwingUtilities.invokeLater(() -> {
                    shapeProgress.put(shape1.getNama(), currentIteration);
                    demoShapeLabel.setText("Shape: " + shape1.getNama());
                    demoStepLabel.setText("Progress: " + currentIteration + " / " + totalIterations);
                    demoProgressBar.setValue((int) ((currentIteration / (double) totalIterations) * 100));
                    demoStatusLabel.setText("Status: " + shape1.getNama() + " sedang menghitung...");
                    shapePreviewPanel.setShapeName(shape1.getClass().getSimpleName());
                    shapePreviewPanel.setProgress(currentIteration, totalIterations);
                    updateDemoLogDisplay(demoShapes, shapeProgress);
                });
            });
        });

        Thread demoThread = new Thread(() -> {
            demoStatusLabel.setText("Status: Demo berjalan...");
            updateDemoLogDisplay(demoShapes, shapeProgress);

            int totalWork = demoShapes.size() * 10;
            int completedTotal = 0;

            while (demoShapes.stream().anyMatch(shape -> !shape.isComplete())) {
                // Shuffle urutan shapes untuk eksekusi random
                List<BendaGeometri> shuffledShapes = new ArrayList<>(demoShapes);
                java.util.Collections.shuffle(shuffledShapes);

                for (BendaGeometri shape : shuffledShapes) {
                    if (shape.isComplete()) {
                        continue;
                    }

                    int before = shape.getCurrentIteration();
                    shape.startWorker();
                    try {
                        shape.getWorker().join(800);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                    if (!shape.isComplete()) {
                        shape.interruptWorker();
                    }

                    int delta = shape.getCurrentIteration() - before;
                    completedTotal += Math.max(delta, 0);
                    final int globalDone = completedTotal;
                    SwingUtilities.invokeLater(() -> {
                        demoProgressBar.setValue((int) ((globalDone / (double) totalWork) * 100));
                        updateDemoLogDisplay(demoShapes, shapeProgress);
                    });

                    if (shape.isComplete()) {
                        try {
                            double luas = shape.hitungLuas();
                            String result = String.format("✓ %s: Luas=%.2f", shape.getNama(), luas);
                            if (shape instanceof VolumeCalculable) {
                                double volume = ((VolumeCalculable) shape).hitungVolume();
                                result += String.format(", Volume=%.2f", volume);
                            }
                            result += String.format(", Keliling=%.2f\n", shape.hitungKeliling());
                            appendDemoResult(result);
                        } catch (Exception ex) {
                            appendDemoResult(String.format("✗ %s: Error - %s\n", shape.getNama(), ex.getMessage()));
                        }
                    }
                }
            }

            SwingUtilities.invokeLater(() -> {
                demoProgressBar.setVisible(false);
                demoStatusLabel.setText("Status: Demo selesai semua shape");
                demoLogArea.setText(demoLogArea.getText() + "\n\n=== Demo Multithreading Selesai ===");
                if (onComplete != null) onComplete.run();
            });
        }, "Demo-Multithreading-Runner");
        demoThread.start();
    }

    private void updateDemoLogDisplay(List<BendaGeometri> shapes, java.util.Map<String, Integer> progress) {
        StringBuilder sb = new StringBuilder();
        sb.append("Memulai program :\n");
        for (int i = 0; i < shapes.size(); i++) {
            BendaGeometri shape = shapes.get(i);
            int currentProgress = progress.getOrDefault(shape.getNama(), 0);
            int totalProgress = shape.getTotalIterations();
            String progressBar = buildProgressBar(currentProgress, totalProgress);
            sb.append(String.format("%2d. %s %s\n", i + 1, shape.getNama(), progressBar));
        }
        demoLogArea.setText(sb.toString());
    }

    private String buildProgressBar(int current, int total) {
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < total; i++) {
            if (i > 0) {
                bar.append("  "); // Jarak antar kotak
            }
            if (i < current) {
                // Kotak terisi dengan █
                bar.append("[█]");
            } else {
                // Kotak kosong
                bar.append("[]");
            }
        }
        return bar.toString();
    }

    private void appendDemoResult(String text) {
        SwingUtilities.invokeLater(() -> {
            demoResultArea.append(text);
            demoResultArea.setCaretPosition(demoResultArea.getDocument().getLength());
        });
    }

    private class ShapePreviewPanel extends JPanel {
        private String shapeName = "-";
        private int completedItems = 0;
        private int totalItems = 10;

        public void setShapeName(String name) {
            this.shapeName = name;
            repaint();
        }

        public void setProgress(int completedItems, int totalItems) {
            this.completedItems = completedItems;
            this.totalItems = totalItems;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(240, 248, 255));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(56, 130, 242));
            g2.setStroke(new BasicStroke(4));

            int w = getWidth();
            int h = getHeight();
            int margin = 25;

            switch (shapeName) {
                case "Elips" -> g2.drawOval(margin, margin + 10, w - margin * 2, h - margin * 3 - 40);
                case "TabungElips" -> {
                    g2.drawOval(margin, margin, w - margin * 2, 40);
                    g2.drawLine(margin, margin + 20, margin, h - margin - 50);
                    g2.drawLine(w - margin, margin + 20, w - margin, h - margin - 50);
                    g2.drawOval(margin, h - margin - 70, w - margin * 2, 40);
                }
                case "KerucutElips", "KerucutTerpancung" -> {
                    g2.drawOval(margin, margin, w - margin * 2, 30);
                    int[] xPoints = {margin, w / 2, w - margin};
                    int[] yPoints = {margin + 20, h - margin - 50, margin + 20};
                    g2.drawPolygon(xPoints, yPoints, 3);
                }
                case "BolaElips" -> g2.drawOval(margin, margin, w - margin * 2, h - margin * 3 - 40);
                case "Juring" -> {
                    g2.drawArc(margin, margin, w - margin * 2, h - margin * 3 - 40, 0, 90);
                    g2.drawLine(w / 2, h / 2, w / 2, margin + 10);
                    g2.drawLine(w / 2, h / 2, w - margin - 10, h / 2);
                }
                case "Tembereng" -> {
                    g2.drawArc(margin, margin, w - margin * 2, h - margin * 3 - 40, 0, 180);
                    g2.drawLine(w / 2, h / 2, w / 2, margin + 10);
                }
                case "CincinElips" -> {
                    g2.drawOval(margin + 10, margin + 10, w - margin * 2 - 20, h - margin * 3 - 60);
                    g2.drawOval(margin + 30, margin + 30, w - margin * 2 - 60, h - margin * 3 - 100);
                }
                case "Juring2Dimensi", "Tembereng2Dimensi", "CincinElips2Dimensi" -> {
                    g2.drawOval(margin, margin, w - margin * 2, h - margin * 3 - 60);
                }
                default -> {
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    FontMetrics fm = g2.getFontMetrics();
                    String label = "Preview";
                    int tx = (w - fm.stringWidth(label)) / 2;
                    int ty = h / 2;
                    g2.drawString(label, tx, ty);
                }
            }

            int bubbleSize = 16;
            int gap = 8;
            int totalWidth = totalItems * bubbleSize + (totalItems - 1) * gap;
            int startX = Math.max(margin, (w - totalWidth) / 2);
            int y = h - margin - 20;

            for (int i = 1; i <= totalItems; i++) {
                if (i <= completedItems) {
                    g2.setColor(new Color(56, 130, 242));
                    g2.fillOval(startX + (i - 1) * (bubbleSize + gap), y, bubbleSize, bubbleSize);
                } else {
                    g2.setColor(new Color(200, 220, 255));
                    g2.fillOval(startX + (i - 1) * (bubbleSize + gap), y, bubbleSize, bubbleSize);
                }
                g2.setColor(new Color(56, 130, 242));
                g2.drawOval(startX + (i - 1) * (bubbleSize + gap), y, bubbleSize, bubbleSize);
            }

            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2.setColor(new Color(56, 130, 242));
            g2.drawString(shapeName, margin, h - margin);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString(String.format("%d / %d data", completedItems, totalItems), w - margin - 80, h - margin);
        }
    }

    private JPanel createInfoPanel(String title, String formula, String description) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(100, 150, 255)), title, 
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 13)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panel.setBackground(new Color(255, 255, 240));

        JTextArea infoArea = new JTextArea(12, 30);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        infoArea.setBackground(new Color(255, 255, 240));
        infoArea.setText(formula + "\n\n" + description);
        infoArea.setMargin(new Insets(10, 10, 10, 10));

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
        JMenuItem runDemoItem = new JMenuItem("Run Multithreading Demo");
        runDemoItem.addActionListener(e -> {
            new Thread(() -> geometry.BendaGeometri.runMultithreadingDemo()).start();
        });
        toolsMenu.add(runDemoItem);
        
        JMenuItem polyDemoItem = new JMenuItem("Demo Polymorphism");
        polyDemoItem.addActionListener(e -> demonstratePolymorphism());
        toolsMenu.add(polyDemoItem);
        
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

    private BolaElips getBolaFromPanel() throws NumberFormatException {
        double a = Double.parseDouble(tfSumbuX.getText().trim());
        double b = Double.parseDouble(tfSumbuY.getText().trim());
        double c = Double.parseDouble(tfSumbuZ.getText().trim());
        if (a <= 0 || b <= 0 || c <= 0) {
            throw new NumberFormatException("Semua nilai sumbu bola harus lebih dari 0");
        }
        return new BolaElips(a, b, c);
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

    private void calculateJuring2D(double a, double b, double sudut) {
        try {
            Juring2Dimensi juring = new Juring2Dimensi(a, b, sudut);
            calculateShape(juring, String.format("a=%.2f, b=%.2f, sudut=%.2f°", a, b, sudut), "Juring (2D)");
        } catch (GeometryException ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private void calculateTembereng2D(double a, double b, double sudut) {
        try {
            Tembereng2Dimensi tembereng = new Tembereng2Dimensi(a, b, sudut);
            calculateShape(tembereng, String.format("a=%.2f, b=%.2f, sudut=%.2f°", a, b, sudut), "Tembereng (2D)");
        } catch (GeometryException ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private void calculateCincin2D(double a1, double b1, double a2, double b2) {
        try {
            CincinElips2Dimensi cincin = new CincinElips2Dimensi(a1, b1, a2, b2);
            calculateShape(cincin, String.format("a1=%.2f, b1=%.2f, a2=%.2f, b2=%.2f", a1, b1, a2, b2), "Cincin Elips (2D)");
        } catch (GeometryException ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private void addToHistory(String shapeName, String parameters, double result, String resultType) {
        String record = String.format("[%tT] %s(%s) = %.4f %s",
                new java.util.Date(), shapeName, parameters, result, resultType);
        calculator.addCalculationRecord(shapeName, parameters, result, resultType);
    }

    private void loadHistory() {
        // History will be shown in dialog
    }

    private void clearHistory() {
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Yakin ingin menghapus semua history?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            calculator.clearHistory();
            statusLabel.setText("History telah dihapus");
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

    private void demonstratePolymorphism() {
        resultArea.setText("");
        statusLabel.setText("Mendemonstrasikan Polimorfisme Runtime...");
        
        StringBuilder output = new StringBuilder();
        output.append("=== DEMONSTRASI POLIMORFISME RUNTIME ===\n");
        
        List<BendaGeometri> shapesCollection = new ArrayList<>();
        
        output.append("[1] Membuat berbagai bentuk geometri:\n");
        output.append("    ✓ Elips (2D)\n");
        shapesCollection.add(new Elips(5.0, 3.0));
        
        output.append("    ✓ KerucutElips (3D)\n");
        shapesCollection.add(new KerucutElips(4.0, 2.0, 6.0));
        
        output.append("    ✓ TabungElips (3D)\n");
        shapesCollection.add(new TabungElips(3.5, 2.5, 8.0));
        
        output.append("    ✓ BolaElips (3D)\n");
        shapesCollection.add(new BolaElips(4.0, 3.0, 2.0));
        
        output.append("    ✓ Juring (3D)\n");
        shapesCollection.add(new Juring(3.0, Math.PI/3, 5.0));
        
        output.append("    ✓ Tembereng (3D)\n");
        shapesCollection.add(new Tembereng(2.0, 5.0));
        
        output.append("    ✓ CincinElips (3D)\n");
        shapesCollection.add(new CincinElips(5.0, 2.0, 1.0));
        
        output.append("\n[2] Semua objek disimpan dalam List<BendaGeometri> (REFERENCE TYPE)\n");
        output.append("    dengan ACTUAL TYPE yang berbeda-beda\n\n");
        
        output.append("[3] Memanggil method info() yang SAMA untuk setiap shape:\n\n");
        
        int index = 1;
        for (BendaGeometri shape : shapesCollection) {
            output.append("───────────────────────────────────────────────────────────────\n");
            output.append("Geometry Shape #").append(index).append(" (Tipe Geometri: ")
                  .append(shape.getClass().getSimpleName()).append(")\n");
            output.append("───────────────────────────────────────────────────────────────\n");
            output.append(shape.info()).append("\n");
            index++;
        }
        
        resultArea.setText(output.toString());
        statusLabel.setText("Demo Polimorfisme selesai! Lihat hasil di atas.");
    }

    private void showHistoryDialog() {
        JDialog dialog = new JDialog(frame, "History Perhitungan", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        list.setFont(new Font("Consolas", Font.PLAIN, 12));
        
        java.util.List<GeometryCalculator.CalculationRecord> records = calculator.getHistory();
        if (records.isEmpty()) {
            model.addElement("=== HISTORY KOSONG ===");
            model.addElement("Lakukan perhitungan untuk menambah history");
        } else {
            model.addElement("=== HISTORY PERHITUNGAN ===");
            for (GeometryCalculator.CalculationRecord record : records) {
                model.addElement(record.toString());
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(list);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnClose = new JButton("Tutup");
        btnClose.addActionListener(e -> dialog.dispose());
        buttonPanel.add(btnClose);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.getContentPane().add(panel);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void showAboutDialog() {
        JDialog dialog = new JDialog(frame, "About", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel(new BorderLayout());
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
                  • Juring Elips (2D)
                  • Tembereng Elips (2D)
                  • Cincin Elips (2D)

                ★ Benda 3 Dimensi (Limas):
                  • Kerucut dengan alas elips
                  • Kerucut terpancung dengan alas elips

                ★ Benda 3 Dimensi (Prisma):
                  • Tabung dengan alas elips
                  • Bola elips (Ellipsoid)
                  • Juring bola (3D)
                  • Tembereng bola (3D)
                  • Cincin dengan pola dasar elips (Torus)

                © 2026 - Proyek PBO Kelompok 3
                Versi 1.0""");
        
        JScrollPane scroll = new JScrollPane(aboutText);
        panel.add(scroll, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton btnClose = new JButton("Tutup");
        btnClose.addActionListener(e -> dialog.dispose());
        buttonPanel.add(btnClose);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.getContentPane().add(panel);
        dialog.setSize(560, 460);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
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