package uk.ac.sheffield.com1003.assignment.gui;

import uk.ac.sheffield.com1003.assignment.codeprovided.League;
import uk.ac.sheffield.com1003.assignment.codeprovided.PlayerEntry;
import uk.ac.sheffield.com1003.assignment.codeprovided.PlayerProperty;
import uk.ac.sheffield.com1003.assignment.codeprovided.gui.AbstractRadarChart;
import uk.ac.sheffield.com1003.assignment.codeprovided.gui.AbstractRadarChartPanel;
import uk.ac.sheffield.com1003.assignment.codeprovided.gui.AbstractPlayerDashboardPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * SKELETON IMPLEMENTATION
 */

public class RadarChartPanel extends AbstractRadarChartPanel {

    private List<Point> axesEndPoint = new ArrayList<>();

    public RadarChartPanel(AbstractPlayerDashboardPanel parentPanel, AbstractRadarChart radarPlot) {
        super(parentPanel, radarPlot);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension d = getSize();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        drawAxisCircles(g2, d);
        drawPropertiesAxes(g2, d);
        drawAxesNamesAndValues(g2, d);
        if (getRadarChart().getFilteredPlayerEntries().size() > 0) {
            drawPlayerValues(g2, d);
            if (getParentPanel().isMinCheckBoxSelected()) drawMin(g2, d);
            if (getParentPanel().isMaxCheckBoxSelected()) drawMax(g2, d);
            if (getParentPanel().isAverageCheckBoxSelected()) drawAvg(g2, d);
        }

        System.out.println("getHeight(): " + getHeight());
    }


    private void drawAxisCircles(Graphics2D g, Dimension d) {
        int minDimension = Collections.min(List.of(d.height, d.width)) - 50;
        BasicStroke dash = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f, new float[]{5f, 5f}, 0f);
        g.setStroke(dash);
        for (int i = 0; i < 5; i++) {
            int dia = minDimension - minDimension / 5 * i;
            int xStart = (d.width / 2) - (dia / 2);
            int yStart = (d.height / 2) - (dia / 2);
            g.drawOval(xStart, yStart, dia, dia);
        }
        g.setStroke(new BasicStroke());
    }

    private void drawPropertiesAxes(Graphics2D g, Dimension d) {
        int maxRadius = (Collections.min(List.of(d.height, d.width)) - 50) / 2;
        int centerX = d.width / 2;
        int centerY = d.height / 2;
        int[] order = {0, -1, 1, -2, 2};
        axesEndPoint.clear();
        for (int i = 0; i < 5; i++) {
            int angle = 180;
            angle += order[i] * 72;
            Point p = new Point((int) (centerX + maxRadius * (Math.sin(Math.toRadians(angle)))), (int) (centerY + maxRadius * (Math.cos(Math.toRadians(angle)))));
            g.drawLine(centerX, centerY, p.x, p.y);
            axesEndPoint.add(p);
        }
    }

    private void drawAxesNamesAndValues(Graphics2D g, Dimension d) {
        int textHeight = g.getFontMetrics().getHeight();
        int maxRadius = (Collections.min(List.of(d.height, d.width)) - 50) / 2;
        int centerX = d.width / 2;
        int centerY = d.height / 2;
        String zero = "0.00";
        int zeroWidth = g.getFontMetrics().stringWidth(zero);
        g.setColor(getBackground());
        g.fillRect(centerX - zeroWidth / 2, centerY - textHeight / 2, zeroWidth, textHeight);
        g.setColor(Color.black);
        g.drawString(zero, centerX - zeroWidth / 2, centerY + textHeight / 4);
        int[] anglesForNames = {0, 72, -72, -36, 36};
        int[] anglesForValues = {0, 72, -72, 144, -144};
        int[] offsets = {1, 1, 1, -1, -1};
        List<PlayerProperty> selectedProperties = getRadarChart().getPlayerRadarChartProperties();
        for (int i = 0; i < 5; i++) {
            Color background = getBackground();
            String textToRender = selectedProperties.get(i).getName();
            int textWidth = g.getFontMetrics().stringWidth(textToRender);
            int x = axesEndPoint.get(i).x;
            int y = axesEndPoint.get(i).y;
            double max = getRadarChart().getPlayerCatalog().getMaximumValue(selectedProperties.get(i), League.ALL);
            g.rotate(Math.toRadians(anglesForValues[i]), centerX, centerY);
            for (int j = 0; j < 5; j++) {
                double val = (max / 5) * (j + 1);
                String valToRender = String.format("%.2f", val);
                int valWidth = g.getFontMetrics().stringWidth(valToRender);
                int valX = centerX - valWidth / 2;
                int valY = centerY + textHeight / 4 - (maxRadius * (j + 1) / 5);
                g.setColor(background);
                // -5 on ValY is padding
                g.fillRect(valX, valY - 5 - textHeight / 2, valWidth, textHeight);
                g.setColor(Color.black);
                g.drawString(valToRender, valX, valY);
            }
            g.rotate(-Math.toRadians(anglesForValues[i]), centerX, centerY);
            g.rotate(Math.toRadians(anglesForNames[i]), x, y);
            g.drawString(textToRender, x - textWidth / 2, y - offsets[i] * textHeight);
            g.rotate(-Math.toRadians(anglesForNames[i]), x, y);
        }
    }


    private void drawMin(Graphics2D g, Dimension d) {
        int centerX = d.width / 2;
        int centerY = d.height / 2;
        int[] propertySequence = {0, 1, 4, 2, 3};
        int[] xPoints = new int[5], yPoints = new int[5];
        List<PlayerProperty> properties = getRadarChart().getPlayerRadarChartProperties();
        for (int i = 0; i < 5; i++) {
            double maxOverall = getRadarChart().getPlayerCatalog().getMaximumValue(properties.get(i), League.ALL);
            double min = getRadarChart().getRadarPlotAxesValues().get(properties.get(i)).getMin();
            double ratioM = min / maxOverall;
            double ratioN = 1 - ratioM;
            xPoints[propertySequence[i]] = (int) (axesEndPoint.get(i).x * ratioM + centerX * ratioN);
            yPoints[propertySequence[i]] = (int) (axesEndPoint.get(i).y * ratioM + centerY * ratioN);
        }
        Polygon polygon = new Polygon(xPoints, yPoints, 5);
        g.setColor(new Color(0, 0, 240, 40));
        g.fillPolygon(polygon);
        g.setColor(Color.blue);
        g.draw(polygon);
        g.setColor(Color.black);
    }

    private void drawMax(Graphics2D g, Dimension d) {
        int centerX = d.width / 2;
        int centerY = d.height / 2;
        int[] propertySequence = {0, 1, 4, 2, 3};
        int[] xPoints = new int[5], yPoints = new int[5];
        List<PlayerProperty> properties = getRadarChart().getPlayerRadarChartProperties();
        for (int i = 0; i < 5; i++) {
            double maxOverall = getRadarChart().getPlayerCatalog().getMaximumValue(properties.get(i), League.ALL);
            double max = getRadarChart().getRadarPlotAxesValues().get(properties.get(i)).getMax();
            double ratioM = max / maxOverall;
            double ratioN = 1 - ratioM;
            xPoints[propertySequence[i]] = (int) (axesEndPoint.get(i).x * ratioM + centerX * ratioN);
            yPoints[propertySequence[i]] = (int) (axesEndPoint.get(i).y * ratioM + centerY * ratioN);
        }
        Polygon polygon = new Polygon(xPoints, yPoints, 5);
        g.setColor(new Color(240, 0, 0, 40));
        g.fillPolygon(polygon);
        g.setColor(Color.red);
        g.draw(polygon);
        g.setColor(Color.black);
    }

    private void drawAvg(Graphics2D g, Dimension d) {
        int centerX = d.width / 2;
        int centerY = d.height / 2;
        int[] propertySequence = {0, 1, 4, 2, 3};
        int[] xPoints = new int[5], yPoints = new int[5];
        List<PlayerProperty> properties = getRadarChart().getPlayerRadarChartProperties();
        for (int i = 0; i < 5; i++) {
            double maxOverall = getRadarChart().getPlayerCatalog().getMaximumValue(properties.get(i), League.ALL);
            double average = getRadarChart().getRadarPlotAxesValues().get(properties.get(i)).getAverage();
            double ratioM = average / maxOverall;
            double ratioN = 1 - ratioM;
            xPoints[propertySequence[i]] = (int) (axesEndPoint.get(i).x * ratioM + centerX * ratioN);
            yPoints[propertySequence[i]] = (int) (axesEndPoint.get(i).y * ratioM + centerY * ratioN);
        }
        Polygon polygon = new Polygon(xPoints, yPoints, 5);
        g.setColor(new Color(0, 240, 0, 40));
        g.fillPolygon(polygon);
        g.setColor(Color.green);
        g.draw(polygon);
        g.setColor(Color.black);
    }

    private void drawPlayerValues(Graphics2D g, Dimension d) {
        int centerX = d.width / 2;
        int centerY = d.height / 2;
        int[] propertySequence = {0, 1, 4, 2, 3};
        g.setColor(Color.orange);
        List<PlayerEntry> playerEntries = getRadarChart().getFilteredPlayerEntries();
        for (PlayerEntry player : playerEntries) {
            int[] xPoints = new int[5], yPoints = new int[5];
            List<PlayerProperty> properties = getRadarChart().getPlayerRadarChartProperties();
            for (int i = 0; i < 5; i++) {
                double maxOverall = getRadarChart().getPlayerCatalog().getMaximumValue(properties.get(i), League.ALL);
                double playerStat = player.getProperty(properties.get(i));
                double ratioM = playerStat / maxOverall;
                double ratioN = 1 - ratioM;
                xPoints[propertySequence[i]] = (int) (axesEndPoint.get(i).x * ratioM + centerX * ratioN);
                yPoints[propertySequence[i]] = (int) (axesEndPoint.get(i).y * ratioM + centerY * ratioN);
            }
            Polygon polygon = new Polygon(xPoints, yPoints, 5);
            g.draw(polygon);
        }
        g.setColor(Color.black);
    }
}

