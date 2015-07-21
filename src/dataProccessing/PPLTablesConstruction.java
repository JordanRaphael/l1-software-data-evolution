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
		
		int versionID=0;
		
		for (Map.Entry<String,PPLSchema> pplSch : allPPLSchemas.entrySet()) {
			
			PPLSchema oneSchema = pplSch.getValue();			
			
			for(int j=0; j<oneSchema.getTables().size(); j++){
				
				PPLTable oneTable=oneSchema.getTableAt(j);
				
				if(!allPPLTables.containsKey(oneTable.getName())){
					oneTable.setBirth(oneSchema.getName());
					oneTable.setBirthVersionID(versionID);
					oneTable.setActive();
					allPPLTables.put(oneTable.getName(),oneTable);
					oneTable=new PPLTable();
					
				}
			}
			
			boolean found=false;
			
			for (Map.Entry<String,PPLTable> pplTbl : allPPLTables.entrySet()) {
				
				found=false;
				
				for(int z=0; z<oneSchema.getTables().size(); z++){
					
					PPLTable oneTable=oneSchema.getTableAt(z);
					if(pplTbl.getKey().equals(oneTable.getName())){
						found=true;
						break;
					}
				}
				
				if(!found && pplTbl.getValue().getActive()){
					pplTbl.getValue().setDeath(oneSchema.getName());
					pplTbl.getValue().setDeathVersionID(versionID);
					pplTbl.getValue().setActive();

				}
				
			}
			
			versionID++;
			
		}
		
		for (Map.Entry<String,PPLTable> pplTbl : allPPLTables.entrySet()) {
			PPLTable oneTable=pplTbl.getValue();
			System.out.println(oneTable.getName()+"\t"+oneTable.getBirth()+"\t"+oneTable.getDeath());
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
