package dataProccessing;

//import hecate.sqlSchema.Schema;
//import hecate.transitions.TransitionList;

import gr.uoi.cs.daintiness.hecate.sql.Schema;
import gr.uoi.cs.daintiness.hecate.transitions.TransitionList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import data.pplSqlSchema.PPLSchema;
import data.pplSqlSchema.PPLTable;
import data.pplTransition.AtomicChange;
import data.pplTransition.PPLTransition;
import data.pplTransition.TableChange;

public class Worker {
	
	private String filename=null;
	private String transitionsFile=null;

	//private GlobalDataKeeper dK;
	private TreeMap<String,PPLSchema> allPPLSchemas = new TreeMap<String,PPLSchema>();
	private TreeMap<String,PPLTable> allTables = new TreeMap<String,PPLTable>();
	private ArrayList<AtomicChange> atomicChanges = new ArrayList<AtomicChange>();
	private TreeMap<String,TableChange> tableChanges = new TreeMap<String,TableChange>();
	private TreeMap<Integer,PPLTransition> allPPLTransitions = new TreeMap<Integer,PPLTransition>();
	
	public Worker(String tmpFilename,String transitionsFile){
		
		filename=tmpFilename;
		this.transitionsFile=transitionsFile;
		
	}

	public void work() throws IOException{
		
		ImportSchemas filesToImportData=new ImportSchemas(filename,transitionsFile);
		try {
			filesToImportData.loadDataset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Schema> allHecSchemas = filesToImportData.getAllHecSchemas();
		ArrayList<TransitionList> allTransitions=filesToImportData.getAllTransitions();

		
		PPLSchemasConstruction pplSchemas = new PPLSchemasConstruction(allHecSchemas);
		pplSchemas.makePPLSchemas();
		allPPLSchemas=pplSchemas.getAllPPLSchemas();
		
		PPLTablesConstruction pplTables = new PPLTablesConstruction(allPPLSchemas);
		pplTables.makeAllPPLTables();
		allTables=pplTables.getAllPPLTables();
		
		AtomicChangeConstruction atomicChangesC = new AtomicChangeConstruction(allTransitions);
		atomicChangesC.makeAtomicChanges();
		atomicChanges=atomicChangesC.getAtomicChanges();
		
		TableChangeConstruction tableChangesC = new TableChangeConstruction(atomicChanges, allTables);
		tableChangesC.makeTableChanges();
		tableChanges=tableChangesC.getTableChanges();
		
		PPLTransitionConstruction pplTransitionC = new PPLTransitionConstruction(allPPLSchemas, tableChanges);
		pplTransitionC.makePPLTransitions();
		allPPLTransitions=pplTransitionC.getAllPPLTransitions();
		
		//this.dK=new GlobalDataKeeper(allPPLSchemas, allTables, atomicChanges, tableChanges, allPPLTransitions,filename);
		
	}
	
//	public GlobalDataKeeper getDataKeeper(){
//		
//		return dK;
//		
//	}
	
	public TreeMap<String,PPLSchema> getAllPPLSchemas(){
		
		return allPPLSchemas;
		
	}
	
	public TreeMap<String,PPLTable> getAllPPLTables(){
		
		return allTables;
		
	}
	
	public ArrayList<AtomicChange> getAtomicChanges(){
		
		return atomicChanges;
		
	}
	
	public TreeMap<String,TableChange> getAllTableChanges(){
		
		return tableChanges;
		
	}
	
	public TreeMap<Integer,PPLTransition> getAllPPLTransitions(){
		
		return allPPLTransitions;
		
	}
	
	public String getDataFolder(){
		return filename.replaceAll(".txt", "");
	}
	
}
