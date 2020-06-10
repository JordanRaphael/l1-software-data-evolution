package data.dataKeeper;

import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.PPLTransition;

public class PPLDataManager {
	
	private TreeMap<String, PPLSchema> allPPLSchemas = null;
	private TreeMap<String, PPLTable> allPPLTables = null;
	private TreeMap<Integer, PPLTransition> allPPLTransitions = null;
	
	protected PPLDataManager() {
		allPPLSchemas = new TreeMap<String, PPLSchema>();
		allPPLTables = new TreeMap<String, PPLTable>();
		allPPLTransitions = new TreeMap<Integer, PPLTransition>();
	}
	
	protected TreeMap<String, PPLSchema> getAllPPLSchemas(){
		return allPPLSchemas;
	}
	
	protected void setAllPPLSchemas(TreeMap<String, PPLSchema> allPPLSchemas){
		this.allPPLSchemas = allPPLSchemas;
	}
	
	protected TreeMap<String, PPLTable> getAllPPLTables(){
		return allPPLTables;
	}
	
	protected void setAllPPLTables(TreeMap<String, PPLTable> allPPLTables){
		this.allPPLTables = allPPLTables;
	}
	
	protected TreeMap<Integer, PPLTransition> getAllPPLTransitions(){
		return allPPLTransitions;
	}
	
	protected void setAllPPLTransitions(TreeMap<Integer, PPLTransition> allPPLTransitions){
		this.allPPLTransitions = allPPLTransitions;
	}
	
	public void printInfo() {
		System.out.println("Schemas:" + getAllPPLSchemas().size());
		System.out.println("Transitions:" + getAllPPLTransitions().size());
		System.out.println("Tables:" + getAllPPLTables().size());
	}
	
	

}
