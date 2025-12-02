import simulation.gui.SimulationGUI;
import simulation.util.SimulationParameters;

import javax.swing.*;
import java.awt.*;

/**
 * Główna klasa uruchamiająca aplikację.
 */
public class Main {

    public static void main(String[] args) {
        // Najpierw pokaż dialog konfiguracyjny
        SwingUtilities.invokeLater(() -> {
            SimulationParameters params = showConfigurationDialog(args);

            if (params != null) {
                SimulationGUI gui = new SimulationGUI(params);
                gui.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }

    /**
     * Pokazuje dialog konfiguracji przed uruchomieniem symulacji.
     */
    private static SimulationParameters showConfigurationDialog(String[] args) {
        SimulationParameters params = new SimulationParameters();

        // Jeśli podano argumenty wiersza poleceń, użyj ich
        if (args.length >= 2) {
            try {
                params.width = Integer.parseInt(args[0]);
                params.height = Integer.parseInt(args[1]);

                if (args.length >= 3) {
                    params.immunityProbability = Double.parseDouble(args[2]);
                }

                // Jeśli argumenty są poprawne, od razu uruchom
                return params;
            } catch (NumberFormatException e) {
                // Błędne argumenty - pokaż dialog
            }
        }

        // Utwórz panel konfiguracji
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Pola tekstowe
        JTextField widthField = new JTextField(String.valueOf(params.width), 10);
        JTextField heightField = new JTextField(String.valueOf(params.height), 10);
        JTextField initialPopField = new JTextField(String.valueOf(params.initialPopulation), 10);
        JTextField initialInfectedField = new JTextField(String.valueOf(params.initialInfected), 10);
        JTextField spawnRateField = new JTextField(String.valueOf(params.spawnRate), 10);

        // Checkbox dla scenariusza z odpornością
        JCheckBox immunityCheckBox = new JCheckBox("Scenariusz 2: Część populacji odporna (30%)");
        immunityCheckBox.addActionListener(e -> {
            params.immunityProbability = immunityCheckBox.isSelected() ? 0.3 : 0.0;
        });

        // Dodaj komponenty do panelu
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Szerokość obszaru (m):"), gbc);
        gbc.gridx = 1;
        panel.add(widthField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Wysokość obszaru (m):"), gbc);
        gbc.gridx = 1;
        panel.add(heightField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Początkowa populacja:"), gbc);
        gbc.gridx = 1;
        panel.add(initialPopField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Początkowa liczba zakażonych:"), gbc);
        gbc.gridx = 1;
        panel.add(initialInfectedField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Częstotliwość spawnu (osób/s):"), gbc);
        gbc.gridx = 1;
        panel.add(spawnRateField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(immunityCheckBox, gbc);

        // Pokaż dialog
        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Konfiguracja Symulacji",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                // Odczytaj wartości
                params.width = Integer.parseInt(widthField.getText());
                params.height = Integer.parseInt(heightField.getText());
                params.initialPopulation = Integer.parseInt(initialPopField.getText());
                params.initialInfected = Integer.parseInt(initialInfectedField.getText());
                params.spawnRate = Double.parseDouble(spawnRateField.getText());

                // Walidacja
                if (params.width < 10 || params.height < 10) {
                    JOptionPane.showMessageDialog(null,
                            "Wymiary muszą być >= 10 metrów!",
                            "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                if (params.initialInfected > params.initialPopulation) {
                    JOptionPane.showMessageDialog(null,
                            "Liczba zakażonych nie może być większa niż populacja!",
                            "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                System.out.println("=== Konfiguracja Symulacji ===");
                System.out.println("Wymiary: " + params.width + " x " + params.height + " m");
                System.out.println("Populacja początkowa: " + params.initialPopulation);
                System.out.println("Zakażeni na starcie: " + params.initialInfected);
                System.out.println("Częstotliwość spawnu: " + params.spawnRate + " osób/s");
                System.out.println("Prawdopodobieństwo odporności: " + (params.immunityProbability * 100) + "%");
                System.out.println("==============================");

                return params;

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Błędne wartości! Wprowadź poprawne liczby.",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        return null; // Anulowano
    }
}