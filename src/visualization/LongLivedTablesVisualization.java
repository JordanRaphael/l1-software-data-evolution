package visualization;

import java.awt.Color;
import java.awt.GradientPaint;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import results.Results;
import sqlSchema.Table;

public class LongLivedTablesVisualization implements Visualization {
	
	private ArrayList<Table> longLivedTables=new ArrayList<Table>();
	private ChartPanel chartPanel=null;
	private Color color=new Color(000,191,255);
	private Results results=null;

	public LongLivedTablesVisualization(){
	  
	}
	
	@SuppressWarnings("unchecked")
	public void draw(Results res){
		results=res;
		longLivedTables=results.getResults();
		final CategoryDataset dataset = createDataset();
		final JFreeChart chart = createChart(dataset);
	    chartPanel = new ChartPanel(chart);
	    chartPanel.setBounds(300, 10, 900, 500);
	}
	
	private CategoryDataset createDataset() {
        
		String[] series=new String[longLivedTables.size()];
		String[] categories=new String[longLivedTables.size()];
		
		for(int i=0;i<series.length;i++){
			series[i]=longLivedTables.get(i).getName();
			categories[i]=longLivedTables.get(i).getName();
		}

        // create the dataset....
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(int i=0;i<series.length;i++){
        	dataset.addValue(longLivedTables.get(i).getAge(), series[i], categories[i]);
        }
        
        return dataset;
        
    }
	
	private JFreeChart createChart(final CategoryDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
            "Age Bar Chart",         // chart title
            "Tables",               // domain axis label
            "Age(Number of Versions)",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);
        
        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(color);
        plot.setRangeGridlinePaint(color);
        
        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setItemMargin(-2);
        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp2 = new GradientPaint(
            0.0f, 0.0f, Color.red, 
            0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp3 = new GradientPaint(
                0.0f, 0.0f, Color.cyan, 
                0.0f, 0.0f, Color.lightGray
            );
        final GradientPaint gp4 = new GradientPaint(
                0.0f, 0.0f, Color.yellow, 
                0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp5 = new GradientPaint(
                0.0f, 0.0f, Color.magenta, 
                0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp6 = new GradientPaint(
                0.0f, 0.0f, Color.orange, 
                0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp7 = new GradientPaint(
                0.0f, 0.0f, Color.pink, 
                0.0f, 0.0f, Color.lightGray
        );
        
        
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        renderer.setSeriesPaint(3, gp3);
        renderer.setSeriesPaint(4, gp4);
        renderer.setSeriesPaint(5, gp5);
        renderer.setSeriesPaint(6, gp6);
        renderer.setSeriesPaint(7, gp7);
        

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
        
    }
	
	public ChartPanel getChart(){
		
		return chartPanel;
		
	} 
	
}
