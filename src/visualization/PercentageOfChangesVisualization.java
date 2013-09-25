package visualization;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import results.Results;
import sqlSchema.Table;

public class PercentageOfChangesVisualization implements Visualization {
	
	private ArrayList<Table> tables=new ArrayList<Table>();
	private Float[][] mo=null;
	private Rotator rotator=null;
	private ChartPanel chartPanel =null;
	private String chartCase="";
	private Results results=null;

	
	 	public PercentageOfChangesVisualization(Float[][] mostIntensive,String tmpCase) {
		    mo=mostIntensive;
		    chartCase=tmpCase;
	    }

		
		@SuppressWarnings("unchecked")
		public void draw(Results res) {
			 	results=res;
			 	tables=results.getResults();
			 
			 
			 	final PieDataset dataset = createDataset();

		        // create the chart...
			 	final JFreeChart chart = ChartFactory.createPieChart(
		    		 chartCase,  // chart title
		    		 dataset,             // dataset
		    		 false,               // include legend
		    		 true,
		    		 false
			 	);

		        // set the background color for the chart...
		        chart.setBackgroundPaint(new Color(222, 222, 255));
		        final PiePlot plot = (PiePlot) chart.getPlot();
		        Color color=new Color(000,191,255);
		        plot.setBackgroundPaint(color);
		        
		        plot.setCircular(true);
		     
		        plot.setNoDataMessage("No data available");

		        // add the chart to a panel...
		        chartPanel= new ChartPanel(chart);
		        
		        if(chartCase.equals("Insersions")){
		        	chartPanel.setBounds(700, 10, 550,180);
		        	
		        }
		        else if(chartCase.equals("Updates")){
		        	chartPanel.setBounds(700, 220, 550, 180);
		        }
		        else if(chartCase.equals("Deletions")){
		        	chartPanel.setBounds(700, 415, 550, 180);
		        }
		        
		        rotator = new Rotator(plot);
		        rotator.start();
		}
	 
	    /**
	     * Creates a sample dataset.
	     * 
	     * @param sections  the number of sections.
	     * 
	     * @return A sample dataset.
	     */
	    private PieDataset createDataset() {
	        final DefaultPieDataset result = new DefaultPieDataset();
	        
	        for(int i=0; i<mo.length; i++){
				
	        	int l=(int)(Math.round(mo[i][0]));
	        	
					final double value = mo[i][1];
					
		            result.setValue("Table " + tables.get(l).getName()+"("+i+")", value);
				
				
			}
	        
	        return result;
	    }

	    /**
	     * The rotator.
	     *
	     */
	    public static class Rotator extends Timer implements ActionListener {

	        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			/** The plot. */
	        private PiePlot plot;

	        /** The angle. */
	        private int angle = 270;

	        /**
	         * Constructor.
	         *
	         * @param plot  the plot.
	         */
	        Rotator(final PiePlot plot) {
	            super(100, null);
	            this.plot = plot;
	            addActionListener(this);
	        }

	        /**
	         * Modifies the starting angle.
	         *
	         * @param event  the action event.
	         */
	        public void actionPerformed(final ActionEvent event) {
	            this.plot.setStartAngle(angle);
	            this.angle = this.angle + 1;
	            if (this.angle == 360) {
	                this.angle = 0;
	            }
	        }

	    }
	    
	    public Rotator getRotator(){
	    	return rotator;
	    }
	    
	    public ChartPanel getChart(){
			
			return chartPanel;
			
		}

	

}
