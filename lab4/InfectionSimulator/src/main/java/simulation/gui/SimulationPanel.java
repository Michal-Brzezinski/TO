package simulation.gui;

import simulation.model.Person;
import simulation.model.SimulationArea;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;

/**
 * Panel rysujący wizualizację symulacji.
 */
public class SimulationPanel extends JPanel {

    private SimulationArea area;
    private int pixelsPerMeter = 15; // Skalowanie: 15 pikseli = 1 metr
    private boolean showVelocityVectors = false;

    public SimulationPanel(SimulationArea area) {
        this.area = area;
        setPreferredSize(new Dimension(
                area.getParams().width * pixelsPerMeter,
                area.getParams().height * pixelsPerMeter
        ));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Włącz antialiasing dla gładszego rysowania
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Rysuj granice obszaru
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(0, 0,
                area.getParams().width * pixelsPerMeter,
                area.getParams().height * pixelsPerMeter);

        // Rysuj siatkę (opcjonalnie)
        drawGrid(g2d);

        // Rysuj osobników
        List<Person> people = area.getPeople();
        for (Person person : people) {
            drawPerson(g2d, person);
        }

        // Rysuj informacje o czasie symulacji
        drawSimulationInfo(g2d);
    }

    /**
     * Rysuje siatkę pomocniczą.
     */
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(new Color(220, 220, 220));
        g2d.setStroke(new BasicStroke(1));

        // Linie pionowe (co 5 metrów)
        for (int x = 0; x <= area.getParams().width; x += 5) {
            int px = x * pixelsPerMeter;
            g2d.drawLine(px, 0, px, area.getParams().height * pixelsPerMeter);
        }

        // Linie poziome (co 5 metrów)
        for (int y = 0; y <= area.getParams().height; y += 5) {
            int py = y * pixelsPerMeter;
            g2d.drawLine(0, py, area.getParams().width * pixelsPerMeter, py);
        }
    }

    /**
     * Rysuje pojedynczego osobnika.
     */
    private void drawPerson(Graphics2D g2d, Person person) {
        double[] pos = person.getPosition().getComponents();
        int x = (int) (pos[0] * pixelsPerMeter);
        int y = (int) (pos[1] * pixelsPerMeter);

        int radius = 8; // Promień okręgu reprezentującego osobnika

        // Ustaw kolor na podstawie stanu
        Color color = person.getCurrentState().getColor();
        g2d.setColor(color);

        // Narysuj okrąg
        Ellipse2D.Double circle = new Ellipse2D.Double(
                x - radius, y - radius, radius * 2, radius * 2);
        g2d.fill(circle);

        // Narysuj kontur
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(circle);

        // Opcjonalnie: rysuj wektor prędkości
        if (showVelocityVectors) {
            drawVelocityVector(g2d, person, x, y);
        }
    }

    /**
     * Rysuje wektor prędkości osobnika (strzałka).
     */
    private void drawVelocityVector(Graphics2D g2d, Person person, int x, int y) {
        double[] vel = person.getVelocity().getComponents();

        // Skaluj wektor prędkości do widocznej długości
        int vx = (int) (vel[0] * pixelsPerMeter * 2);
        int vy = (int) (vel[1] * pixelsPerMeter * 2);

        g2d.setColor(new Color(100, 100, 100, 150));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(x, y, x + vx, y + vy);

        // Rysuj grot strzałki
        drawArrowhead(g2d, x, y, x + vx, y + vy);
    }

    /**
     * Rysuje grot strzałki.
     */
    private void drawArrowhead(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowSize = 6;

        int[] xPoints = new int[3];
        int[] yPoints = new int[3];

        xPoints[0] = x2;
        yPoints[0] = y2;
        xPoints[1] = (int) (x2 - arrowSize * Math.cos(angle - Math.PI / 6));
        yPoints[1] = (int) (y2 - arrowSize * Math.sin(angle - Math.PI / 6));
        xPoints[2] = (int) (x2 - arrowSize * Math.cos(angle + Math.PI / 6));
        yPoints[2] = (int) (y2 - arrowSize * Math.sin(angle + Math.PI / 6));

        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    /**
     * Rysuje informacje o symulacji.
     */
    private void drawSimulationInfo(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));

        int x = 10;
        int y = 20;
        int lineHeight = 18;

        g2d.drawString(String.format("Czas: %.1fs", area.getSimulationTime()), x, y);
        y += lineHeight;
        g2d.drawString(String.format("Populacja: %d", area.getTotalCount()), x, y);
    }

    // Settery

    public void setArea(SimulationArea area) {
        this.area = area;
        setPreferredSize(new Dimension(
                area.getParams().width * pixelsPerMeter,
                area.getParams().height * pixelsPerMeter
        ));
        revalidate();
    }

    public void setShowVelocityVectors(boolean show) {
        this.showVelocityVectors = show;
    }

    public void setPixelsPerMeter(int pixelsPerMeter) {
        this.pixelsPerMeter = pixelsPerMeter;
        setPreferredSize(new Dimension(
                area.getParams().width * pixelsPerMeter,
                area.getParams().height * pixelsPerMeter
        ));
        revalidate();
    }
}