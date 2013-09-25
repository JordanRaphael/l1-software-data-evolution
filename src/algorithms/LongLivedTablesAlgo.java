package algorithms;

import java.util.ArrayList;
import java.util.TreeMap;

import results.LongLivedTablesResults;
import results.Results;
import sqlSchema.Schema;
import sqlSchema.Table;

public class LongLivedTablesAlgo implements Algorithm {
	
	private ArrayList<Schema> AllSchemas=new ArrayList<Schema>();
	private int k=0;
	private Results results=null;
	
	public LongLivedTablesAlgo(ArrayList<Schema> tmpAllSchemas , int tmpk){
		
		AllSchemas=tmpAllSchemas;
		k=tmpk;
	
	}
	
	public Results compute(){
		
		ArrayList<Table> longLivedTables=new ArrayList<Table>();
		ArrayList<String> tableNames=new ArrayList<String>();
		
		
		
		int found=0;
		int age=0;
		
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
					
					age=calculateAge(currentTable);
					tableNames.add(currentTableName);
					AllSchemas.get(i).getTableAt(j).setAge(age);
					longLivedTables.add(currentTable);
					currentTable=new Table();
					age=0;
					
				}
				else if(found==1){
					
					found=0;
					currentTable=new Table();
				
				}
				
			}
			
		}

		longLivedTables=sortTablesByAge(longLivedTables);
		
		for(int i=longLivedTables.size()-1; i>=k; i--){
			longLivedTables.remove(i);
		}
		
		results=new LongLivedTablesResults();
		results.setResults(longLivedTables);
		
		return results;
		
		
	}

	private int calculateAge(Table currentTable) {
		
		TreeMap<String,Table> tables=new TreeMap<String,Table>();
		
		int age=0;
		
		for(int u=0; u<AllSchemas.size(); u++){
			
			tables=AllSchemas.get(u).getTables();
			
			if(tables.containsKey(currentTable.getName())){
				age++;
			}
			
		}
		
		return age;
		
		
	}
	
	private ArrayList<Table> sortTablesByAge(ArrayList<Table> tmpLongLivedTables) {
		
		ArrayList<Table> sortingTables=new ArrayList<Table>();
		
		for(int i=0; i<tmpLongLivedTables.size(); i++){
			
			if(i==0){
				
				sortingTables.add(tmpLongLivedTables.get(i));
			
			}
			else{
				
				for(int j=0; j<sortingTables.size(); j++){
					
					if(tmpLongLivedTables.get(i).getAge()>sortingTables.get(j).getAge()){
						sortingTables.add(j, tmpLongLivedTables.get(i));
						break;
					}
					else if(j==sortingTables.size()-1){
						sortingTables.add(tmpLongLivedTables.get(i));
						break;
					}
					
				}
				
				
			}
			
		}
		
		return sortingTables;
	}
	

}
