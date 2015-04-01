package visualization;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import data.pplSqlSchema.PPLTable;
import results.Results;

public class IntensiveTablesFromTwoSchemasVisualization implements Visualization {
	
	
	private ChartPanel chartPanel=null;
	private ArrayList<PPLTable> mostIntensiveTables=new ArrayList<PPLTable>();
	private Results results=null;

	
	public  IntensiveTablesFromTwoSchemasVisualization(){
		 //super(title);
	     
	     //setContentPane(chartPanel);
	}
	
	@SuppressWarnings("unchecked")
	public void draw(Results res){
		 results=res;
		 mostIntensiveTables=results.getResults();
		
	     final CategoryDataset dataset = createDataset();
	     final JFreeChart chart = createChart(dataset);
	     chartPanel= new ChartPanel(chart);
	     chartPanel.setBounds(500, 10, 700, 500);
	}

	private CategoryDataset createDataset() {

        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for(int i=0;i<mostIntensiveTables.size();i++){
        	dataset.addValue(mostIntensiveTables.get(i).getCurrentChanges(), mostIntensiveTables.get(i).getName(),"");
        }
        
        /*dataset.addValue(25.0, "Series 1", "Category 1");   
        dataset.addValue(34.0, "Series 1", "Category 2");   
        dataset.addValue(19.0, "Series 2", "Category 1");   
        dataset.addValue(29.0, "Series 2", "Category 2");   
        dataset.addValue(41.0, "Series 3", "Category 1");   
        dataset.addValue(33.0, "Series 3", "Category 2");  */ 
        return dataset;

    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(final CategoryDataset dataset) {
        
        final JFreeChart chart = ChartFactory.createBarChart3D(
            "For These Versions",      // chart title
            "Tables",               // domain axis label
            "Changes",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        final CategoryPlot plot = chart.getCategoryPlot();
        final CategoryAxis axis = plot.getDomainAxis();
        axis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 8.0)
        );
        
        final CategoryItemRenderer renderer = plot.getRenderer();
        final BarRenderer r = (BarRenderer) renderer;
        r.setMaximumBarWidth(0.05);
        
        return chart;

    }
    
    public ChartPanel getChart(){
		
		return chartPanel;
		
	} 
	
	
}
