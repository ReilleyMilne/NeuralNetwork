import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class Test {

    public static JFrame frame;
    public static XYSeriesCollection dataset;
    public static JFreeChart lineChart;
    public static ChartPanel lineChartPanel;

    public static int sampleInterval = 10;

    public static void createGraph(int epochs, double[] costForEpoch) {
        dataset = new XYSeriesCollection();

        XYSeries series = new XYSeries("Neural Network Outputs");
        sampleInterval = 10;
        for (int i = 1; i <= epochs; i++) {
            if (i % sampleInterval == 0) {
                series.add(i, costForEpoch[i - 1]);
            }
        }
        dataset.addSeries(series);

        lineChart = ChartFactory.createXYLineChart(
                "Neural Network Output Classification", // Title
                "Epochs",                               // X-Axis Label
                "Cost",                                 // Y-Axis Label
                dataset,                                // Dataset
                PlotOrientation.VERTICAL,               // Orientation
                true,                                   // Show Legend
                true,                                   // Tooltips
                false                                   // URLs
        );

        // Configure the Y-axis as log scale
        NumberAxis yAxis = (NumberAxis) lineChart.getXYPlot().getRangeAxis();
        yAxis.setLabel("Cost (Log Scale)");

        NumberAxis xAxis = (NumberAxis) lineChart.getXYPlot().getDomainAxis();
        xAxis.setNumberFormatOverride(new DecimalFormat("#"));

        // Create the line chart panel
        lineChartPanel = new ChartPanel(lineChart);
    }

    public static void addSeries(int epochs, double[] yData, String seriesName) {
        XYSeries series = new XYSeries(seriesName);
        sampleInterval = 10;
        for (int i = 1; i <= epochs; i++) {
            if (i % sampleInterval == 0) {
                series.add(i, yData[i - 1]);
            }
        }
        dataset.addSeries(series);
        lineChartPanel.revalidate();
        lineChartPanel.repaint();
    }

    public static void createScatterPlot(DataPoint[] dataPoints) {
        XYSeriesCollection scatterDataset = new XYSeriesCollection();
        XYSeries redPoints = new XYSeries("Red Points");
        XYSeries bluePoints = new XYSeries("Blue Points");


        // Add points to the corresponding series based on their labels
        for (int i = 0; i < dataPoints.length; i++) {
            DataPoint dataPoint = dataPoints[i];
            double x = dataPoint.inputs[0];
            double y = dataPoint.inputs[1];
            if (dataPoint.label == 0) {
                redPoints.add(x, y);
            } else {
                bluePoints.add(x, y);
            }
        }

        scatterDataset.addSeries(redPoints);
        scatterDataset.addSeries(bluePoints);

        // Create the scatter plot
        JFreeChart scatterChart = ChartFactory.createScatterPlot(
                "Labeled Data Points",                  // Title
                "X-Axis Label",                         // X-Axis Label
                "Y-Axis Label",                         // Y-Axis Label
                scatterDataset,                         // Dataset
                PlotOrientation.VERTICAL,               // Orientation
                true,                                   // Show Legend
                true,                                   // Tooltips
                false                                   // URLs
        );

        // Customize renderer to set colors
        XYPlot scatterPlot = scatterChart.getXYPlot();
        XYItemRenderer renderer = new XYLineAndShapeRenderer(false, true);
        renderer.setSeriesPaint(0, Color.RED);   // Red points
        renderer.setSeriesPaint(1, Color.BLUE);  // Blue points
        scatterPlot.setRenderer(renderer);

        // Create a new panel for the scatter plot
        ChartPanel scatterChartPanel = new ChartPanel(scatterChart);
        scatterChartPanel.setPreferredSize(new Dimension(800, 400));

        // Add both chart panels to a single main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(lineChartPanel);
        mainPanel.add(scatterChartPanel);

        // Create the main frame
        frame = new JFrame("Charts");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
