//package visualization;
//
//import java.awt.Color;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import javax.swing.JOptionPane;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Marshaller;
//import javax.xml.bind.Unmarshaller;
//
//import org.antlr.v4.runtime.RecognitionException;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.axis.CategoryAxis;
//import org.jfree.chart.axis.CategoryLabelPositions;
//import org.jfree.chart.axis.NumberAxis;
//import org.jfree.chart.axis.ValueAxis;
//import org.jfree.chart.plot.CategoryPlot;
//import org.jfree.chart.renderer.category.CategoryItemRenderer;
//import org.jfree.chart.renderer.category.CategoryStepRenderer;
//import org.jfree.data.category.CategoryDataset;
//import org.jfree.data.general.DatasetUtilities;
//
//import parser.Delta;
//import parser.DiffResult;
//import parser.HecateParser;
//import pplSqlSchema.PPLTable;
//import sqlSchema.Schema;
//import transitions.Deletion;
//import transitions.Insersion;
//import transitions.Transition;
//import transitions.TransitionList;
//import transitions.Transitions;
//import transitions.Update;
//
//public class CategoryChart {
//	
//	
//	private ArrayList<PPLTable> tables=new ArrayList<PPLTable>();
//	private ArrayList<Schema> schemas=new ArrayList<Schema>();
//	private ChartPanel chartPanel=null;
//	private static ArrayList<TransitionList> Transitions=new ArrayList<TransitionList>();
//	private ArrayList<PPLTable> mostIntensiveTables=new ArrayList<PPLTable>();
//	private String[] Versions=null;
//	private String[] lala=null;
//	double[][] data=null;
//	
//	public CategoryChart(ArrayList<Schema> tmpSchemas,ArrayList<PPLTable> tmpTables) throws IOException, RecognitionException{
//		
//		schemas=tmpSchemas;
//		tables=tmpTables;
//		makeDifferencies();
//		calculateChangesforTheseVersions();
//		fillVersions();
//		fillTableNames();
//		fillData();
//		
//		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(lala,Versions, data);
//			        
//		  
//		        // create the chart...
//		        final JFreeChart chart = createChart(dataset);
//		        chartPanel = new ChartPanel(chart);
//		        chartPanel.setBounds(300, 10, 1000, 500);
//		        
//		        
//		      
//		    }
//		    /**
//		     * Creates a chart.
//		     * 
//		     * @param dataset  the dataset.
//		     * 
//		     * @return The chart.
//		     */
//	  private JFreeChart createChart(final CategoryDataset dataset) {
//		            
//		        final CategoryItemRenderer renderer = new CategoryStepRenderer(true);
//		        final CategoryAxis domainAxis = new CategoryAxis("Versions");
//		        final ValueAxis rangeAxis = new NumberAxis("Value");
//		        final CategoryPlot plot = new CategoryPlot(dataset, domainAxis, rangeAxis, renderer);
//		        final JFreeChart chart = new JFreeChart("Category Step Chart", plot);
//
//		        chart.setBackgroundPaint(Color.white);
//		        
//		       
//		        
//		        plot.setBackgroundPaint(Color.gray);
//		        plot.setDomainGridlinesVisible(true);
//		        plot.setDomainGridlinePaint(Color.white);
//		        plot.setRangeGridlinesVisible(true);
//		        plot.setRangeGridlinePaint(Color.white);
//		        
//		        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);
//		        domainAxis.setLowerMargin(0.0);
//		        domainAxis.setUpperMargin(0.0);
//		        domainAxis.addCategoryLabelToolTip("Type 1", "The first type.");
//		        domainAxis.addCategoryLabelToolTip("Type 2", "The second type.");
//		        domainAxis.addCategoryLabelToolTip("Type 3", "The third type.");
//		        
//		        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//		        rangeAxis.setLabelAngle(0 * Math.PI / 2.0);
//		        // OPTIONAL CUSTOMISATION COMPLETED.
//				return chart;
//	}
//	
//		    
//		    private void makeDifferencies() throws IOException, RecognitionException{
//				
//				String sStandardString="HecateAppData"+File.separator;
//				Transitions trs = new Transitions();
//				
//				for(int i=0; i<schemas.size(); i++){
//		        	
//		        	if(i==schemas.size()-1){
//		        		String sFinalString2=sStandardString+schemas.get(i).getName();
//		            	Schema schema=HecateParser.parse(sFinalString2);
//		            	schema.setTitle(schemas.get(i).getName());
//		        		break;
//		        	}
//		        	String sFinalString=sStandardString+schemas.get(i).getName().trim();
//		        	
//		        	String sFinalString2=sStandardString+schemas.get(i+1).getName().trim();
//		        	
//		        	Schema oldSchema = HecateParser.parse(sFinalString);
//		    		oldSchema.setTitle(schemas.get(i).getName().trim());
//		    		Schema newSchema = HecateParser.parse(sFinalString2);
//		    		newSchema.setTitle(schemas.get(i+1).getName().trim());
//		    		
//		    		Delta delta = new Delta();
//		    		
//		    		TransitionList tmpTransitionList =new TransitionList();
//		    		@SuppressWarnings("static-access")
//					DiffResult tmpDiffRes=delta.minus(oldSchema, newSchema);
//		    		tmpTransitionList=tmpDiffRes.tl;
//		    		trs.add(tmpTransitionList);
//		    		
//		        }
//				
//				test(trs);
//				
//			}
//			
//			
//			private static void test(Transitions tl) throws FileNotFoundException {
//				try {
//					
//				
//					
//					JAXBContext jaxbContext = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
//					Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//					jaxbMarshaller.marshal(tl, new FileOutputStream("TransitionsForCharts.xml"));
//					
//					
//					//***********************************************
//					
//					JAXBContext jaxbContext1 = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
//					Unmarshaller u=jaxbContext1.createUnmarshaller();
//					File inputFile=new File("TransitionsForCharts.xml");
//					Transitions root = (Transitions) u.unmarshal(inputFile);
//					
//					
//					Transitions=(ArrayList<TransitionList>) root.getList();
//					
//					
//					
//				} catch (JAXBException e) {
//					JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
//					return;
//					//e.printStackTrace();
//				}
//			}
//			
//			
//			private ArrayList<PPLTable> calculateChangesforTheseVersions(){
//				
//				
//				ArrayList<String> tableName=new ArrayList<String>();
//				
//				
//				for(int j=0; j<tables.size(); j++){
//						
//					PPLTable currentTable=tables.get(j);	
//						
//					currentTable.setChangesForChart(calculateChangesForTable(currentTable));
//					mostIntensiveTables.add(currentTable);
//					tableName.add(currentTable.getName());
//					currentTable=new PPLTable();
//					
//							
//							
//				}
//					
//				for(int i=0;i<mostIntensiveTables.size();i++){
//					for(int k=0;k<mostIntensiveTables.get(i).getChangesForChart().size();k++){
//						System.out.println(mostIntensiveTables.get(i).getName()+"   "+mostIntensiveTables.get(i).getChangesForChart().get(k));
//					}
//				}
//				
//				
//				return mostIntensiveTables;
//				
//				
//			}
//			
//			private ArrayList<Integer> calculateChangesForTable(PPLTable currentTable){
//				
//				
//				int assistantChanges=0;
//				ArrayList<Integer> changesForThisTable=new ArrayList<Integer>();
//				
//				for(int j=0;j<Transitions.size();j++){
//					ArrayList<Transition> transitions=Transitions.get(j).getList();
//					if(transitions!=null){
//						for(int i=0; i<transitions.size(); i++){
//							
//							if(currentTable.getName().equals(transitions.get(i).getAffTable().getName())){
//								assistantChanges=assistantChanges+transitions.get(i).getNumOfAffAttributes();
//							}
//							
//						}
//					}
//					
//					changesForThisTable.add(assistantChanges);
//					assistantChanges=0;
//					
//				}
//				return changesForThisTable;
//			
//			}	    
//			
//			
//			public ChartPanel getChart(){
//				
//				return chartPanel;
//				
//			}
//			
//	private void fillVersions(){
//		
//		Versions=new String[schemas.size()];
//		
//		for(int i=0;i<schemas.size();i++){
//			Versions[i]=schemas.get(i).getName();
//		}
//	}
//	
//	private void fillTableNames(){
//		
//		lala=new String[mostIntensiveTables.size()];
//		
//		for(int i=0;i<mostIntensiveTables.size();i++){
//			lala[i]=mostIntensiveTables.get(i).getName();
//			
//		}
//		
//	}
//		   
//	private void fillData(){
//		
//		data=new double[mostIntensiveTables.size()][Versions.length];
//		
//		for(int i=0;i<mostIntensiveTables.size();i++){
//			ArrayList<Integer> tmp=mostIntensiveTables.get(i).getChangesForChart();
//			
//			for(int j=0;j<tmp.size();j++){
//				data[i][j]=tmp.get(j);
//			}
//		}	
//	}
//	
//
//}
