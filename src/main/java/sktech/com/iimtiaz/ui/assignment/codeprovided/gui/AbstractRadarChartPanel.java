package sktech.com.iimtiaz.ui.assignment.codeprovided.gui;

import javax.swing.*;

/**
 * Abstract class for displaying a RadarChart.
 * Please pay attention to the note comment below the constructor.
 * The displayed radar chart should be updated whenever a radar chart is updated
 */
public class AbstractRadarChartPanel extends JPanel {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final AbstractPlayerDashboardPanel parentPanel;
    private final AbstractRadarChart radarChart;
    public AbstractRadarChartPanel(AbstractPlayerDashboardPanel parentPanel, AbstractRadarChart radarChart) {
        super();
        this.parentPanel = parentPanel;
        this.radarChart = radarChart;
    }

    /* NOTE: your RadarChartPanel must override JPanel's `protected void paintComponent(Graphics g)`,
    in order to redraw your Radar Chart whenever it is updated.
    For example:

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension d = getSize();
        Graphics2D g2 = (Graphics2D) g;

        Line2D l = new Line2D.Double(
            0,
            0,
            d.width,
            d.height
        );
        g2.draw(l);
    }

     */

    public AbstractRadarChart getRadarChart()
    {
        return radarChart;
    }

    public AbstractPlayerDashboardPanel getParentPanel()
    {
        return parentPanel;
    }
}

