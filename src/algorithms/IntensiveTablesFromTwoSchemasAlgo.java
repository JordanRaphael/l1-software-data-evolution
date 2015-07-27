package algorithms;

import java.util.ArrayList;
import java.util.TreeMap;

import data.pplSqlSchema.PPLSchema;
import data.pplSqlSchema.PPLTable;
import data.pplTransition.AtomicChange;
import data.pplTransition.TableChange;
import results.Results;
import results.ResultsFactory;

public class IntensiveTablesFromTwoSchemasAlgo implements Algorithm {
	
	
	private PPLSchema firstSchema=null;
	private PPLSchema secondSchema=null;
	private int k=0;
	private TreeMap<String,TableChange> tableChForTwoSchemas=null;
	private Results results=null;
	private ArrayList<PPLTable> mostIntensiveTables=new ArrayList<PPLTable>();

	
	public IntensiveTablesFromTwoSchemasAlgo(PPLSchema tmpFirstSchema,PPLSchema tmpSecondSchema,TreeMap<String,TableChange> tmpTableCh,int tmpk){
	
		firstSchema=tmpFirstSchema;
		secondSchema=tmpSecondSchema;
		tableChForTwoSchemas=tmpTableCh;
		k=tmpk;
	}
	
//	public void setAll(PPLSchema tmpFirstSchema,PPLSchema tmpSecondSchema,TreeMap<String,TableChange> tmpTableCh,int tmpk){
//		
//		firstSchema=tmpFirstSchema;
//		secondSchema=tmpSecondSchema;
//		tableChForTwoSchemas=tmpTableCh;
//		k=tmpk;
//		
//	}
	
	public Results compute(){
		
		ArrayList<PPLSchema> currentSchemas=new ArrayList<PPLSchema>();
		currentSchemas.add(firstSchema);
		currentSchemas.add(secondSchema);
		
		ArrayList<String> tableName=new ArrayList<String>();
		
		int found=0;
		int tableCurrentChanges=0;
		
		for(int i=0;i<currentSchemas.size(); i++){
			
			PPLSchema oneSchema=currentSchemas.get(i);
			
			for(int j=0; j<oneSchema.getTables().size(); j++){
				
				PPLTable currentTable=oneSchema.getTableAt(j);	
					
				for(int l=0; l<tableName.size(); l++){
						
					if(currentTable.getName().equals(tableName.get(l))){
						found=1;
						break;
					}
					
				}
	
				if(found==1){
					found=0;
					currentTable=new PPLTable();
				}
				else{
					
					tableCurrentChanges=calculateChangesForThisTable(currentTable);
					currentTable.setCurrentChanges(tableCurrentChanges);
					mostIntensiveTables.add(currentTable);
					tableName.add(currentTable.getName());
					currentTable=new PPLTable();
					tableCurrentChanges=0;
					
					
				}
			}
			
		}
		
		
		mostIntensiveTables=sortByCurrentChanges(mostIntensiveTables);
		
		for(int i=mostIntensiveTables.size()-1; i>=k; i--){
			mostIntensiveTables.remove(i);
		}
		
		ResultsFactory rf = new ResultsFactory("IntensiveTablesFromTwoSchemaResults");
		results=rf.createResult();
		results.setResults(mostIntensiveTables);
		
		return results;
		
		
	}
	
	private int calculateChangesForThisTable(PPLTable currentTable){
		
		int changesForThisTable=0;
		
		TableChange t=tableChForTwoSchemas.get(currentTable.getName());

		TreeMap<String, ArrayList<AtomicChange>> atChs=t.getTableAtomicChanges();
		
		ArrayList<AtomicChange> atChsAr=atChs.get(secondSchema.getName());
		
		changesForThisTable=atChsAr.size();
				
		return changesForThisTable;
	
	}
	
	private ArrayList<PPLTable> sortByCurrentChanges(ArrayList<PPLTable> mostIntensiveTables){
		
		ArrayList<PPLTable> sortingTables=new ArrayList<PPLTable>();
		
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



	@Override
	public void compute(String compute) {
		// TODO Auto-generated method stub
		
	}



}
