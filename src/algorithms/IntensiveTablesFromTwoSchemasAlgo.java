package algorithms;

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

import parser.Delta;
import parser.HecateParser;
import results.IntensiveTablesFromTwoSchemasResults;
import results.Results;
import sqlSchema.Schema;
import sqlSchema.Table;
import transitions.Deletion;
import transitions.Insersion;
import transitions.Transition;
import transitions.TransitionList;
import transitions.Transitions;
import transitions.Update;

public class IntensiveTablesFromTwoSchemasAlgo implements Algorithm {
	
	
	private static ArrayList<TransitionList> CurrentTransitions=new ArrayList<TransitionList>();
	private Schema firstSchema=null;
	private Schema secondSchema=null;
	private String projectDataFolder=null;
	private int k=0;
	private Results results=null;
	
	public IntensiveTablesFromTwoSchemasAlgo(
			Schema tmpFirstSchema,Schema tmpSecondSchema,String tmpProject , int tmpk){

		firstSchema=tmpFirstSchema;
		secondSchema=tmpSecondSchema;
		projectDataFolder=tmpProject;
		k=tmpk;
		
	}
	
	public void findDifferenciesFromTwoSchemas() throws IOException{
		
		String sStandardString=projectDataFolder+File.separator;
		Transitions trs = new Transitions();
		
		String sFinalString=sStandardString+firstSchema.getName();
    	
    	String sFinalString2=sStandardString+secondSchema.getName();
    	
    	Schema oldSchema = HecateParser.parse(sFinalString);
		oldSchema.setTitle(firstSchema.getName());
		Schema newSchema = HecateParser.parse(sFinalString2);
		newSchema.setTitle(secondSchema.getName());
		
		Delta delta = new Delta();
		
		TransitionList tmpTransitionList =new TransitionList();
		tmpTransitionList=delta.minus(oldSchema, newSchema); 
		trs.add(tmpTransitionList);
		
		test(trs);
		
		//calculateChangesforTheseVersions();
		
		
		
	}
	
	private static void test(Transitions tl) throws FileNotFoundException {
		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(tl, new FileOutputStream("TransitionsBetweenTwoVersions.xml"));
			
			
			//***********************************************
			
			JAXBContext jaxbContext1 = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Unmarshaller u=jaxbContext1.createUnmarshaller();
			File inputFile=new File("TransitionsBetweenTwoVersions.xml");
			Transitions root = (Transitions) u.unmarshal(inputFile);
			
			CurrentTransitions=(ArrayList<TransitionList>) root.getList();
			
			
		} catch (JAXBException e) {
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
			//e.printStackTrace();
		}
	}
	
	public Results compute(){
		
		ArrayList<Table> mostIntensiveTables=new ArrayList<Table>();
		ArrayList<Schema> currentSchemas=new ArrayList<Schema>();
		currentSchemas.add(firstSchema);
		currentSchemas.add(secondSchema);
		
		ArrayList<String> tableName=new ArrayList<String>();
		
		int found=0;
		int tableCurrentChanges=0;
		
		for(int i=0;i<currentSchemas.size(); i++){
			
			Schema oneSchema=currentSchemas.get(i);
			
			for(int j=0; j<oneSchema.getTables().size(); j++){
				
				Table currentTable=oneSchema.getTableAt(j);	
					
				for(int l=0; l<tableName.size(); l++){
						
					if(currentTable.getName().equals(tableName.get(l))){
						found=1;
						break;
					}
					
				}
	
				if(found==1){
					found=0;
					currentTable=new Table();
				}
				else{
					
					tableCurrentChanges=calculateChangesForThisTable(currentTable);
					currentTable.setCurrentChanges(tableCurrentChanges);
					mostIntensiveTables.add(currentTable);
					tableName.add(currentTable.getName());
					currentTable=new Table();
					tableCurrentChanges=0;
					
					
				}
			}
			
		}
		
		
		mostIntensiveTables=sortByCurrentChanges(mostIntensiveTables);
		
		for(int i=mostIntensiveTables.size()-1; i>=k; i--){
			mostIntensiveTables.remove(i);
		}
		
		
		results=new IntensiveTablesFromTwoSchemasResults();
		results.setResults(mostIntensiveTables);
		
		return results;
		
		
	}
	
	private int calculateChangesForThisTable(Table currentTable){
		
		int changesForThisTable=0;
		
		ArrayList<Transition> transitions=CurrentTransitions.get(0).getList();
		if(transitions!=null){
			for(int i=0; i<transitions.size(); i++){
				
				if(currentTable.getName().equals(transitions.get(i).getAffTable().getName())){
					changesForThisTable=changesForThisTable+transitions.get(i).getNumOfAffAttributes();
				}
				
			}
		}
		return changesForThisTable;
	
	}
	
	private ArrayList<Table> sortByCurrentChanges(ArrayList<Table> mostIntensiveTables){
		
		ArrayList<Table> sortingTables=new ArrayList<Table>();
		
		for(int i=0; i<mostIntensiveTables.size(); i++){
			
			if(i==0){
				
				sortingTables.add(mostIntensiveTables.get(i));
			
			}
			else{
				
				for(int j=0; j<sortingTables.size(); j++){
					
					if(mostIntensiveTables.get(i).getCurrentChanges()>sortingTables.get(j).getCurrentChanges()){
						sortingTables.add(j, mostIntensiveTables.get(i));
						break;
					}
					else if(j==sortingTables.size()-1){
						sortingTables.add(mostIntensiveTables.get(i));
						break;
					}
					
				}
				
				
			}
			
		}
		
		return sortingTables;
		
	}
	
	

}
