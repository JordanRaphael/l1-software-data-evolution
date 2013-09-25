package algorithms;

import java.util.ArrayList;

import results.MostUpdatedTablesResults;
import results.Results;
import sqlSchema.Schema;
import sqlSchema.Table;

public class MostUpdatedTablesAlgo implements Algorithm {
	
	private ArrayList<Schema> AllSchemas=new ArrayList<Schema>();
	private int k=0;
	private Results results=null;
	
	public MostUpdatedTablesAlgo(ArrayList<Schema> tmpAllSchemas, int tmpk){
		
		AllSchemas=tmpAllSchemas;
		k=tmpk;
		
	}
	
	public Results compute(){
		
		ArrayList<Table> mostUpdatedTables=new ArrayList<Table>();
		ArrayList<String> tableNames=new ArrayList<String>();
		
		int found=0;
		
		for(int i=0; i<AllSchemas.size(); i++){
			
			Schema oneSchema=AllSchemas.get(i);
			
			for(int j=0; j<oneSchema.getTables().size(); j++){
				
				Table currentTable=oneSchema.getTableAt(j);
				String currentTableName=currentTable.getName();
				
				for(int k=0; k<tableNames.size(); k++){
					
					if(currentTableName.equals(tableNames.get(k))){
						
						found=1;
						break;
					
					}
				}
				
				if(found==0){
					
					tableNames.add(currentTableName);
					mostUpdatedTables.add(currentTable);
					currentTable=new Table();
					
					
				}
				else if(found==1){
					
					found=0;
					currentTable=new Table();
				
				}
				
			}
			
		}

		mostUpdatedTables=sortTablesByTotalChanges(mostUpdatedTables);
		
		for(int i=mostUpdatedTables.size()-1; i>=k; i--){
			mostUpdatedTables.remove(i);
		}
		
		results=new MostUpdatedTablesResults();
		results.setResults(mostUpdatedTables);
		
		return results;
		
		
	}

	private ArrayList<Table> sortTablesByTotalChanges(ArrayList<Table> tmpMostUpdatedTables) {
		
		ArrayList<Table> sortingTables=new ArrayList<Table>();
		
		for(int i=0; i<tmpMostUpdatedTables.size(); i++){
			
			if(i==0){
				
				sortingTables.add(tmpMostUpdatedTables.get(i));
			
			}
			else{
				
				for(int j=0; j<sortingTables.size(); j++){
					
					if(tmpMostUpdatedTables.get(i).getTotalChanges()>sortingTables.get(j).getTotalChanges()){
						sortingTables.add(j, tmpMostUpdatedTables.get(i));
						break;
					}
					else if(j==sortingTables.size()-1){
						sortingTables.add(tmpMostUpdatedTables.get(i));
						break;
					}
					
				}
				
				
			}
			
		}
		
		return sortingTables;
	}

}
