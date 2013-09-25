package visualization;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.CategoryLabelWidthType;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.text.TextBlockAnchor;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import results.Results;
import sqlSchema.Attribute;

public class MostUpdatedAttributesVisualization implements Visualization {

	

	    // ****************************************************************************
	    // * JFREECHART DEVELOPER GUIDE                                               *
	    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
	    // * to purchase from Object Refinery Limited:                                *
	    // *                                                                          *
	    // * http://www.object-refinery.com/jfreechart/guide.html                     *
	    // *                                                                          *
	    // * Sales are used to provide funding for the JFreeChart project - please    * 
	    // * support us so that we can continue developing free software.             *
	    // ****************************************************************************
	    
	    /**
	     * Creates a new demo.
	     *
	     * @param title  the frame title.
	     */
	
		private ChartPanel chartPanel=null;
		private Results results=null;
	
	    public MostUpdatedAttributesVisualization() {

	    }
	    
	    @SuppressWarnings("unchecked")
		public void draw(Results res){
	    	results=res;
	    	ArrayList<Attribute> attributes=results.getResults();
	   
	    	
	    	final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    	
	    	for(int i=0; i<attributes.size(); i++){
	    		 dataset.addValue(attributes.get(i).getTotalAttributeChanges(), attributes.get(i).getName(),"");
	    	}
	    	
	        final JFreeChart chart = createChart(dataset);
	        
	        // add the chart to a panel...
	        chartPanel = new ChartPanel(chart);
	        chartPanel.setBounds(650, 10, 610, 500);
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
	            "Most updated attributes",       // chart title
	            "Attribute",                  // domain axis label
	            "Value",                     // range axis label
	            dataset,                     // data
	            PlotOrientation.HORIZONTAL,  // orientation
	            true,                        // include legend
	            true,                        // tooltips
	            false                        // urls
	        );

	        final CategoryPlot plot = chart.getCategoryPlot();
	        plot.setForegroundAlpha(1.0f);

	        // left align the category labels...
	        final CategoryAxis axis = plot.getDomainAxis();
	        final CategoryLabelPositions p = axis.getCategoryLabelPositions();
	        
	        final CategoryLabelPosition left = new CategoryLabelPosition(
	            RectangleAnchor.LEFT, TextBlockAnchor.CENTER_LEFT, 
	            TextAnchor.CENTER_LEFT, 0.0,
	            CategoryLabelWidthType.RANGE, 0.30f
	        );
	        axis.setCategoryLabelPositions(CategoryLabelPositions.replaceLeftPosition(p, left));
	        
	        return chart;        
	    
	    }
	
	
		public ChartPanel getChart() {
		
			return chartPanel;
		}

		
		
}
