package algorithms;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.pplSqlSchema.PPLSchema;
import data.pplSqlSchema.PPLTable;
import results.Results;
import results.ResultsFactory;

public class LongLivedTablesAlgo implements Algorithm {
	
	private TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private int k=0;
	private Results results=null;
	private ArrayList<PPLTable> longLivedTables=new ArrayList<PPLTable>();

	public LongLivedTablesAlgo(TreeMap<String,PPLSchema> tmpAllSchemas , int tmpk){
		
		allPPLSchemas=tmpAllSchemas;
		k=tmpk;
	
	}
	
//	public void setAll(TreeMap<String,PPLSchema> tmpAllSchemas , int tmpk){
//		
//		allPPLSchemas=tmpAllSchemas;
//		k=tmpk;
//	}
	
	public Results compute(){
		
		ArrayList<String> tableNames=new ArrayList<String>();
		
		
		
		int found=0;
		int age=0;
		
		for (Map.Entry<String,PPLSchema> pplSc : allPPLSchemas.entrySet()) {
			
			PPLSchema oneSchema = pplSc.getValue();
					
			for(int j=0; j<oneSchema.getTables().size(); j++){
				
				PPLTable currentTable=oneSchema.getTableAt(j);
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
					allPPLSchemas.get(pplSc.getKey()).getTableAt(j).setAge(age);
					longLivedTables.add(currentTable);
					currentTable=new PPLTable();
					age=0;
					
				}
				else if(found==1){
					
					found=0;
					currentTable=new PPLTable();
				
				}
				
			}
			
		}

		longLivedTables=sortTablesByAge(longLivedTables);
		
		for(int i=longLivedTables.size()-1; i>=k; i--){
			longLivedTables.remove(i);
		}
		

		ResultsFactory rf = new ResultsFactory("LongLivedTablesResults");
		results=rf.createResult();
		results.setResults(longLivedTables);
		
		return results;
		
		
	}

	private int calculateAge(PPLTable currentTable) {
		
		TreeMap<String,PPLTable> tables=new TreeMap<String,PPLTable>();
		
		int age=0;
		
		for (Map.Entry<String,PPLSchema> pplSc : allPPLSchemas.entrySet()) {
			
			tables=allPPLSchemas.get(pplSc.getKey()).getTables();
			
			if(tables.containsKey(currentTable.getName())){
				age++;
			}
			
		}
		
		return age;
		
		
	}
	
	private ArrayList<PPLTable> sortTablesByAge(ArrayList<PPLTable> tmpLongLivedTables) {
		
		ArrayList<PPLTable> sortingTables=new ArrayList<PPLTable>();
		
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

	@Override
	public void compute(String compute) {
		// TODO Auto-generated method stub
		
	}

	
	

}
