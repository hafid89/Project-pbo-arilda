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
        frame.setSize(950, 750);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        UIManager.put("Button.background", new Color(56, 130, 242));
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 12));
        UIManager.put("TabbedPane.background", new Color(245, 248, 255));
        UIManager.put("TabbedPane.selected", new Color(220, 235, 255));
        UIManager.put("Panel.background", new Color(248, 251, 255));
        UIManager.put("TextField.background", new Color(255, 255, 255));
        UIManager.put("TextArea.background", new Color(250, 250, 255));

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

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabbedPane.setBackground(new Color(245, 248, 255));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        tabbedPane.addTab("Elips (2D)", create2DPanel());
        tabbedPane.addTab("Kerucut Elips", create3DLimasPanel());
        tabbedPane.addTab("Kerucut Terpancung", createKerucutTerpancungPanel());
        tabbedPane.addTab("Tabung Elips", createTabungElipsPanel());
        tabbedPane.addTab("Bola Elips", createBolaElipsPanel());
        tabbedPane.addTab("Juring", createJuringPanel());
        tabbedPane.addTab("Tembereng", createTemberengPanel());
        tabbedPane.addTab("Cincin Elips", createCincinPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        statusLabel = new JLabel("Siap melakukan perhitungan");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(progressBar, BorderLayout.CENTER);

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(153, 180, 255)), "Hasil Perhitungan"),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        resultPanel.setBackground(new Color(255, 255, 255));

        resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        resultArea.setBackground(new Color(250, 250, 255));

        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultPanel.add(resultScroll, BorderLayout.CENTER);

        mainPanel.add(resultPanel, BorderLayout.SOUTH);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel create2DPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Sumbu Panjang (a):"), gbc);
        gbc.gridx = 1;
        tfSumbuPanjang = new JTextField(10);
        panel.add(tfSumbuPanjang, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Sumbu Pendek (b):"), gbc);
        gbc.gridx = 1;
        tfSumbuPendek = new JTextField(10);
        panel.add(tfSumbuPendek, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnHitung = new JButton("Hitung");
        JButton btnReset = new JButton("Reset");

        btnHitung.addActionListener(e -> calculateElips());
        btnReset.addActionListener(e -> {
            tfSumbuPanjang.setText("");
            tfSumbuPendek.setText("");
        });

        btnHitung.setBackground(new Color(56, 130, 242));
        btnHitung.setForeground(Color.BLACK);
        btnHitung.setFocusPainted(false);

        btnReset.setBackground(new Color(220, 53, 69));
        btnReset.setForeground(Color.BLACK);
        btnReset.setFocusPainted(false);

        buttonPanel.add(btnHitung);
        buttonPanel.add(btnReset);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        JPanel infoPanel = createInfoPanel("Elips",
                "Rumus:\n• Luas = π × a × b\n• Keliling ≈ π × [3(a+b) - √((3a+b)(a+3b))]",
                "Keterangan:\na = sumbu panjang\nb = sumbu pendek");

        gbc.gridy = 3;
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
        historyListModel.add(0, record);
        calculator.addCalculationRecord(shapeName, parameters, result, resultType);
    }

    private void loadHistory() {
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