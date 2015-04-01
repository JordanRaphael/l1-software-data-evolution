package algorithms;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.pplSqlSchema.PPLSchema;
import data.pplSqlSchema.PPLTable;
import results.Results;
import results.ResultsFactory;

public class MostUpdatedTablesAlgo implements Algorithm {
	
	private TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private int k=0;
	private Results results=null;
	private ArrayList<PPLTable> mostUpdatedTables=new ArrayList<PPLTable>();

	
	public MostUpdatedTablesAlgo(TreeMap<String,PPLSchema> tmpAllSchemas, int tmpk){
		
		allPPLSchemas=tmpAllSchemas;
		k=tmpk;
		
	}
	
//	public void setAll(TreeMap<String,PPLSchema> tmpAllSchemas, int tmpk){
//		allPPLSchemas=tmpAllSchemas;
//		k=tmpk;
//		
//	}
	
	public Results compute(){
		
		ArrayList<String> tableNames=new ArrayList<String>();
		
		int found=0;
		
		for (Map.Entry<String,PPLSchema> pplSc : allPPLSchemas.entrySet()) {
			
			PPLSchema oneSchema=pplSc.getValue();
			
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
					
					tableNames.add(currentTableName);
					mostUpdatedTables.add(currentTable);
					currentTable=new PPLTable();
					
					
				}
				else if(found==1){
					
					found=0;
					currentTable=new PPLTable();
				
				}
				
			}
			
		}

		mostUpdatedTables=sortTablesByTotalChanges(mostUpdatedTables);
		
		for(int i=mostUpdatedTables.size()-1; i>=k; i--){
			mostUpdatedTables.remove(i);
		}

		ResultsFactory rf = new ResultsFactory("MostUpdatedTablesResults");
		results=rf.createResult();
		results.setResults(mostUpdatedTables);
		
		return results;
		
		
	}

	private ArrayList<PPLTable> sortTablesByTotalChanges(ArrayList<PPLTable> tmpMostUpdatedTables) {
		
		ArrayList<PPLTable> sortingTables=new ArrayList<PPLTable>();
		
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

	@Override
	public void compute(String compute) {
		// TODO Auto-generated method stub
		
	}


}
