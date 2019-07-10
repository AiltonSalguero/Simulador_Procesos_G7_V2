package ventanas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class BarsFrame extends JPanel {

    public static void main(String... args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BarsFrame().setVisible(true);
            }
        });
    }

    public BarsFrame() {
        DefaultCategoryDataset barChartData = new DefaultCategoryDataset();
        barChartData.setValue(20, "Eje Y", "Eje X");
        barChartData.setValue(1, "Eje Y", "Eje Xs");
        barChartData.setValue(15, "Eje Y", "Eje Xs");

        JFreeChart barChart = ChartFactory.createBarChart("", "Proceso", "Progreso", barChartData, PlotOrientation.HORIZONTAL, false, true, true);
        CategoryPlot barChartPlot = barChart.getCategoryPlot();
        barChartPlot.setRangeGridlinePaint(java.awt.Color.RED);
        barChartPlot.isDomainPannable();

        BarRenderer br = (BarRenderer) barChartPlot.getRenderer();
        br.setMaximumBarWidth(.15);

        ChartPanel bar_FCFS = new ChartPanel(barChart);
        this.add(bar_FCFS);
        setSize(400, 100);
    }
}
