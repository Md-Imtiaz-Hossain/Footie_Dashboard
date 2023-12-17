package sktech.com.iimtiaz.ui.assignment.codeprovided.gui;

/**
 * One of the axis of a radar chart.
 * See AbstractRadarChart for more details.
 */

public class RadarAxisValues {
    private final double min;
    private final double max;
    private final double average;

    public RadarAxisValues(double min, double max, double average) {
        this.min = min;
        this.max = max;
        this.average = average;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getAverage() {
        return average;
    }

}

