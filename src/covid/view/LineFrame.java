package covid.view;

import covid.model.Coviddata;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineFrame {
    
    // κλαση υπευθυνη για τη δημιουργια του διαγραμματος με χρηση του JFreeChart
    // εχουμε 2 constructors ο ενας χρησιμοποιειται οταν η κλαση καλειται για μια
    // χωρα μονο, ενω η overloaded version οταν καλειται με πολλαπλες χωρες
    
    // κυρια μεθοδος δημιουργιας ενος διαγραμματος
    // ο κωδικας χτιστηκε με βαση την δοσμενη υλοποιηση απο την εκφωνηση
    public LineFrame(String title, SortedMap<LocalDate, Coviddata> map, String dataType) {

        final CategoryDataset dataset = createDataset(map, dataType);
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(900, 450));

        JFrame frame = new JFrame(title);
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(0, 5));
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    // overloaded method constructor 
    public LineFrame(String title, HashMap<String, SortedMap<LocalDate, Coviddata>> list) {

        final CategoryDataset dataset = createDataset(list);
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(900, 450));

        JFrame frame = new JFrame(title);
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(0, 5));

        frame.add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private CategoryDataset createDataset(HashMap<String, SortedMap<LocalDate, Coviddata>> list) {
        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        double temp;
        int disperseRate = 0;

        for (Map.Entry<String, SortedMap<LocalDate, Coviddata>> entry : list.entrySet()) {
            // μετρητης για την δειγματοληψια
            int i = 0;

            // SortedMap <LocalDate, Coviddata> = entry.getValue
            SortedMap<LocalDate, Coviddata> tempMap = entry.getValue();

            // προσθηκη λογικης: οταν εχουμε πολλα δεδομενα στον οριζοντιο αξονα  δεν φαινονται 
            // τα labels, και ειναι δυσαναγνωστο το διαγραμμα. προταση μας ειναι να μειωνουμε 
            // τον ογκο σε πολλα δεδομενα στα περιπου σε 10-12 τυχαια δείγματα
            // δημιουργουμε ενα δεικτη δειγματοληψιας 1/10 του συνολικου ογκου δεδομενων
            if (tempMap.keySet().size() > 15) {

                temp = ((double) tempMap.keySet().size()) / 10;

                temp = ((double) tempMap.keySet().size()) / temp;

                disperseRate = (int) Math.ceil(temp);

            }

            for (LocalDate date : tempMap.keySet()) {
                // εαν ο δεικτης δειγματοληψιας ειναι μεγαλυτερος του μηδεν
                if (disperseRate > 0) {
                    if (i == 0) {
                        // σε καθε μηδενισμο του μετρητη θα προσθετουμε τα δεδομενα
                        dataset.addValue(tempMap.get(date).getQty(), entry.getKey(), date.toString());
                    }
                    i++;
                    // επαυξηση του μετρητη
                    if (i % disperseRate == 0) {
                        i = 0;
                    }
                } else {
                    // η μη υπαρξη του δεικτη σηματοδοτει δεδομενα με μικρο ογκο (10<) αρα απευθειας προσθηκη
                    // στη λιστα εμφανισης στατιστικων του διαγράμματος
                    dataset.addValue(tempMap.get(date).getQty(), entry.getKey(), date.toString());
                }

            }

        }

        return dataset;

    }

    private CategoryDataset createDataset(SortedMap<LocalDate, Coviddata> map, String dataType) {

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // row keys...
        final String series1 = dataType;

        // προσθηκη λογικης: οταν εχουμε πολλα δεδομενα στον οριζοντιο αξονα  δεν φαινονται 
        // τα labels, και ειναι δυσαναγνωστο το διαγραμμα. προταση μας ειναι να μειωνουμε 
        // τον ογκο σε πολλα δεδομενα στα περιπου σε 10-12 τυχαια δείγματα
        // δημιουργουμε ενα δεικτη δειγματοληψιας 1/10 του συνολικου ογκου δεδομενων
        double temp;
        int disperseRate = 0;
        int i = 0;

        if (map.keySet().size() > 15) {

            temp = ((double) map.keySet().size()) / 10;

            temp = ((double) map.keySet().size()) / temp;

            disperseRate = (int) Math.ceil(temp);

        }

        // confirmed
        // column keys
        for (LocalDate date : map.keySet()) {

            // εαν ο δεικτης δειγματοληψιας ειναι μεγαλυτερος του μηδεν
            if (disperseRate > 0) {
                if (i == 0) {
                    // σε καθε μηδενισμο του μετρητη θα προσθετουμε τα δεδομενα
                    dataset.addValue(map.get(date).getQty(), dataType, date.toString());
                }
                i++;
                // επαυξηση του μετρητη
                if (i % disperseRate == 0) {
                    i = 0;
                }
            } else {
                // η μη υπαρξη του δεικτη σηματοδοτει δεδομενα με μικρο ογκο (10<) αρα απευθειας προσθηκη
                // στη λιστα εμφανισης στατιστικων του διαγράμματος
                dataset.addValue(map.get(date).getQty(), dataType, date.toString());
            }
        }
        return dataset;
    }

    private JFreeChart createChart(final CategoryDataset dataset) {

        // create the chart...
        final JFreeChart chart = ChartFactory.createLineChart(
                "Line Chart Covid19 Stats", // chart title
                "Time", // domain axis label
                "Quantity", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips
                false // urls
        );

        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);

        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        // εμφανιση των ημερομηνιων καθετα. βοηθαει οπτικα σε μεγαλο πληθος επιλογων
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

        renderer.setSeriesStroke(
                0, new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );

        return chart;
    }
}
