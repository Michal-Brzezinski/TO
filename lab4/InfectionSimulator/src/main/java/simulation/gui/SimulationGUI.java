package simulation.gui;

import simulation.controller.SimulationEngine;
import simulation.memento.Caretaker;
import simulation.model.SimulationArea;
import simulation.util.SimulationParameters;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Główne okno GUI aplikacji.
 */
public class SimulationGUI extends JFrame {

    private SimulationArea area;
    private SimulationEngine engine;
    private SimulationPanel simulationPanel;

    // Komponenty GUI
    private JButton startButton;
    private JButton pauseButton;
    private JButton resetButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton stepButton;
    private JCheckBox velocityVectorsCheckBox;

    private JLabel statsLabel;
    private JLabel timeLabel;

    public SimulationGUI(SimulationParameters params) {
        this.area = new SimulationArea(params);
        this.engine = new SimulationEngine(area);

        initComponents();
        setupCallbacks();

        setTitle("Symulacja Zakażeń - Infection Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Inicjalizuje komponenty GUI.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel symulacji (środek)
        simulationPanel = new SimulationPanel(area);
        JScrollPane scrollPane = new JScrollPane(simulationPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Panel kontrolny (góra)
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        // Panel statystyk (dół)
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.SOUTH);
    }

    /**
     * Tworzy panel z przyciskami kontrolnymi.
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        startButton = new JButton("▶ Start");
        startButton.addActionListener(e -> engine.start());

        pauseButton = new JButton("⏸ Pauza");
        pauseButton.addActionListener(e -> engine.pause());

        stepButton = new JButton("⏭ Krok");
        stepButton.addActionListener(e -> {
            engine.pause();
            engine.step();
        });

        resetButton = new JButton("🔄 Reset");
        resetButton.addActionListener(e -> resetSimulation());

        saveButton = new JButton("💾 Zapisz");
        saveButton.addActionListener(e -> saveSimulation());

        loadButton = new JButton("📁 Wczytaj");
        loadButton.addActionListener(e -> loadSimulation());

        velocityVectorsCheckBox = new JCheckBox("Pokaż wektory prędkości");
        velocityVectorsCheckBox.addActionListener(e -> {
            simulationPanel.setShowVelocityVectors(velocityVectorsCheckBox.isSelected());
            simulationPanel.repaint();
        });

        panel.add(startButton);
        panel.add(pauseButton);
        panel.add(stepButton);
        panel.add(resetButton);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(velocityVectorsCheckBox);

        return panel;
    }

    /**
     * Tworzy panel ze statystykami.
     */
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        timeLabel = new JLabel("Czas: 0.0s");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 14));

        statsLabel = new JLabel();
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        updateStatistics();

        panel.add(timeLabel);
        panel.add(statsLabel);

        return panel;
    }

    /**
     * Ustawia callbacki silnika.
     */
    private void setupCallbacks() {
        engine.setOnUpdateCallback(() -> {
            simulationPanel.repaint();
            updateStatistics();
        });
    }

    /**
     * Aktualizuje etykiety ze statystykami.
     */
    private void updateStatistics() {
        timeLabel.setText(String.format("Czas: %.1fs", area.getSimulationTime()));

        int total = area.getTotalCount();
        int healthy = area.getHealthyCount();
        int infected = area.getInfectedCount();
        int immune = area.getImmuneCount();

        statsLabel.setText(String.format(
                "Populacja: %d | 🟢 Zdrowi: %d | 🔴 Zakażeni: %d | 🔵 Odporni: %d | Spawned: %d | Removed: %d",
                total, healthy, infected, immune, area.getTotalSpawned(), area.getTotalRemoved()
        ));
    }

    /**
     * Resetuje symulację.
     */
    private void resetSimulation() {
        engine.pause();
        area.reset();
        simulationPanel.repaint();
        updateStatistics();
        JOptionPane.showMessageDialog(this,
                "Symulacja zresetowana!",
                "Reset",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Zapisuje stan symulacji do pliku.
     */
    private void saveSimulation() {
        engine.pause();

        try {
            String filename = Caretaker.saveSimulation(area);
            JOptionPane.showMessageDialog(this,
                    "Symulacja zapisana do: " + filename,
                    "Zapis",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Błąd zapisu: " + ex.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Wczytuje stan symulacji z pliku.
     */
    private void loadSimulation() {
        engine.pause();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Pliki symulacji (*.sav)", "sav"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                area = Caretaker.loadSimulation(file.getAbsolutePath());
                engine = new SimulationEngine(area);
                setupCallbacks();
                simulationPanel.setArea(area);
                simulationPanel.repaint();
                updateStatistics();

                JOptionPane.showMessageDialog(this,
                        "Symulacja wczytana z: " + file.getName(),
                        "Wczytano",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this,
                        "Błąd wczytywania: " + ex.getMessage(),
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
