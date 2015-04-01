package dataProccessing;

import java.util.Map;
import java.util.TreeMap;

import data.pplSqlSchema.PPLSchema;
import data.pplSqlSchema.PPLTable;
import data.pplTransition.TableChange;

public class PPLTablesConstruction {
	
	private static TreeMap<String,PPLSchema> allPPLSchemas = new TreeMap<String,PPLSchema>();
	private static TreeMap<String,PPLTable> allPPLTables = new TreeMap<String,PPLTable>();

	public PPLTablesConstruction(TreeMap<String,PPLSchema> tmpAllPPLSchemas){
		
		allPPLSchemas=tmpAllPPLSchemas;
		
	}
	
	public void makeAllPPLTables(){
		
		for (Map.Entry<String,PPLSchema> pplSch : allPPLSchemas.entrySet()) {
			
			PPLSchema oneSchema = pplSch.getValue();
			
			for(int j=0; j<oneSchema.getTables().size(); j++){
				
				PPLTable oneTable=oneSchema.getTableAt(j);
				
				if(!allPPLTables.containsKey(oneTable.getName())){
					
					allPPLTables.put(oneTable.getName(),oneTable);
					oneTable=new PPLTable();
					
				}
			}
		}
		
		
	}
	
	public void matchTableChanges(TreeMap<String,TableChange> allTableChanges){
		
		for (Map.Entry<String, TableChange> t : allTableChanges.entrySet()) {
			
			TableChange tmpTableChange = t.getValue();
			
			allPPLTables.get(t.getKey()).setTableChanges(tmpTableChange);
				
		}
		
	}
	
	public TreeMap<String,PPLTable> getAllPPLTables(){
		
		return allPPLTables;
		
	}
	
	

}
