package visualization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.antlr.v4.runtime.RecognitionException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import parser.Delta;
import parser.HecateParser;
import results.Results;
import sqlSchema.Schema;
import sqlSchema.Table;
import transitions.Deletion;
import transitions.Insersion;
import transitions.Transition;
import transitions.TransitionList;
import transitions.Transitions;
import transitions.Update;

public class MostUpdatedTablesVisualization implements Visualization {
	
	private ArrayList<Table> tables=new ArrayList<Table>();
	private ArrayList<Schema> schemas=new ArrayList<Schema>();
	private ChartPanel chartPanel=null;
	private static ArrayList<TransitionList> Transitions=new ArrayList<TransitionList>();
	private ArrayList<Table> mostIntensiveTables=new ArrayList<Table>();
	private String projectDataFolder=null;
	private XYSeriesCollection data =null;
	private Results results=null;

	public MostUpdatedTablesVisualization(ArrayList<Schema> tmpSchemas, String tmpProject) throws IOException{
		schemas=tmpSchemas;
		projectDataFolder=tmpProject;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void draw(Results res) {
		results=res;
		tables=results.getResults();
		try {
			makeDifferencies();
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	private void makeDifferencies() throws IOException, RecognitionException{
		
		String sStandardString=projectDataFolder+File.separator;
		Transitions trs = new Transitions();
		
		for(int i=0; i<schemas.size(); i++){
        	
        	if(i==schemas.size()-1){
        		String sFinalString2=sStandardString+schemas.get(i).getName();
            	Schema schema=HecateParser.parse(sFinalString2);
            	schema.setTitle(schemas.get(i).getName());
        		break;
        	}
        	String sFinalString=sStandardString+schemas.get(i).getName().trim();
        	
        	String sFinalString2=sStandardString+schemas.get(i+1).getName().trim();
        	
        	Schema oldSchema = HecateParser.parse(sFinalString);
    		oldSchema.setTitle(schemas.get(i).getName().trim());
    		Schema newSchema = HecateParser.parse(sFinalString2);
    		newSchema.setTitle(schemas.get(i+1).getName().trim());
    		
    		Delta delta = new Delta();
    		
    		TransitionList tmpTransitionList =new TransitionList();
    		tmpTransitionList=delta.minus(oldSchema, newSchema); 
    		trs.add(tmpTransitionList);
    		
        }
		
		test(trs);
		
	}
	
	
	private static void test(Transitions tl) throws FileNotFoundException {
		try {
			
		
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(tl, new FileOutputStream("TransitionsForCharts.xml"));
			
			
			//***********************************************
			
			JAXBContext jaxbContext1 = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Unmarshaller u=jaxbContext1.createUnmarshaller();
			File inputFile=new File("TransitionsForCharts.xml");
			Transitions root = (Transitions) u.unmarshal(inputFile);
			
			
			Transitions=(ArrayList<TransitionList>) root.getList();
			
			
			
		} catch (JAXBException e) {
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
			//e.printStackTrace();
		}
	}
	
	
	private ArrayList<Table> calculateChangesforTheseVersions(){
		
		
		ArrayList<String> tableName=new ArrayList<String>();
		
		
		for(int j=0; j<tables.size(); j++){
				
			Table currentTable=tables.get(j);	
				
			currentTable.setChangesForChart(calculateChangesForTable(currentTable));
			mostIntensiveTables.add(currentTable);
			tableName.add(currentTable.getName());
			currentTable=new Table();
					
					
		}
		
		return mostIntensiveTables;
		
		
	}
	
	private ArrayList<Integer> calculateChangesForTable(Table currentTable){
		
		
		int assistantChanges=0;
		ArrayList<Integer> changesForThisTable=new ArrayList<Integer>();
		
		for(int j=0;j<Transitions.size();j++){
			ArrayList<Transition> transitions=Transitions.get(j).getList();
			if(transitions!=null){
				for(int i=0; i<transitions.size(); i++){
					
					if(currentTable.getName().equals(transitions.get(i).getAffTable().getName())){
						System.out.println(currentTable.getName()+"   "+transitions.get(i).getNumOfAffAttributes());
						assistantChanges=assistantChanges+transitions.get(i).getNumOfAffAttributes();
					}
					
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
			System.out.println(tables.get(i).getName()+"************ " +data.getSeriesCount());
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
