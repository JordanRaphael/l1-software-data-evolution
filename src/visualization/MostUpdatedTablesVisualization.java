package visualization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import data.pplSqlSchema.PPLTable;
import data.pplTransition.PPLTransition;
import data.pplTransition.TableChange;
import results.Results;

public class MostUpdatedTablesVisualization implements Visualization {
	
	private ArrayList<PPLTable> tables=new ArrayList<PPLTable>();
	private ChartPanel chartPanel=null;
	private TreeMap<String,PPLTransition> allPPLTransitions = new TreeMap<String, PPLTransition>();
	private ArrayList<PPLTable> mostIntensiveTables=new ArrayList<PPLTable>();
	private XYSeriesCollection data =null;
	private Results results=null;

	public MostUpdatedTablesVisualization(TreeMap<String,PPLTransition> tmpAllPPLTransitions) throws IOException{
		
		allPPLTransitions=tmpAllPPLTransitions;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void draw(Results res) {
		results=res;
		tables=results.getResults();
		calculateChangesforTheseVersions();
		
		data = new XYSeriesCollection();
		
		fillXYSeries();
		
	    final JFreeChart chart = ChartFactory.createXYLineChart(
	            "Most Updated Tables",
	            "Version ID", 
	            "Number of Changes", 
	            data,
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            false
	    );
	    
	    XYPlot plot = chart.getXYPlot();
	    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
	    renderer.setLegendItemToolTipGenerator(
	    new StandardXYSeriesLabelGenerator("Legend {1}"));
	    chartPanel = new ChartPanel(chart);
	    chartPanel.setBounds(300, 10, 900, 500);
	} 
	

	
	
	private ArrayList<PPLTable> calculateChangesforTheseVersions(){
		
		ArrayList<String> tableName=new ArrayList<String>();
		
		for(int j=0; j<tables.size(); j++){
				
			PPLTable currentTable=tables.get(j);	
				
			currentTable.setChangesForChart(calculateChangesForTable(currentTable));
			mostIntensiveTables.add(currentTable);
			tableName.add(currentTable.getName());
			currentTable=new PPLTable();
					
		}
		
		return mostIntensiveTables;
		
	}
	
	private ArrayList<Integer> calculateChangesForTable(PPLTable currentTable){
		
		int assistantChanges=0;
		ArrayList<Integer> changesForThisTable=new ArrayList<Integer>();
		
		for(Map.Entry<String,PPLTransition> tr:allPPLTransitions.entrySet()){
			
			PPLTransition trans=tr.getValue();
			ArrayList<TableChange> tableCh = trans.getTableChanges();
			for(int i=0; i<tableCh.size(); i++){
				
				if(tableCh.get(i).getAffectedTableName().equals(currentTable.getName())){
					System.out.println(tableCh.get(i).getTableAtChForOneTransition().size());
					assistantChanges=assistantChanges+tableCh.get(i).getTableAtChForOneTransition().size();
					
				}
				
			}
			
			changesForThisTable.add(assistantChanges);
			assistantChanges=0;
			
		}
		
		return changesForThisTable;
	
	}
	
	
	public void fillXYSeries(){
				
		data.removeAllSeries();
		
		for(int i=0;i<tables.size();i++){
			
			final XYSeries series = new XYSeries(tables.get(i).getName());
			
			for(int j=0;j<mostIntensiveTables.get(i).getChangesForChart().size();j++){
				series.add(j,mostIntensiveTables.get(i).getChangesForChart().get(j));
			}
			
			int found=0;
			for(int k=0;k<data.getSeriesCount();k++){
				if(data.getSeries(k)==series){
					found=1;
					break;
				}
			}
			
			if(found==0){
				data.addSeries(series);
			}
		}
				
	}
	
	
	public ChartPanel getChart(){
		
		return chartPanel;
		
	}

	
   
}
