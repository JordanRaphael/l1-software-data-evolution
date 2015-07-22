package data.dataKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import data.pplSqlSchema.PPLSchema;
import data.pplSqlSchema.PPLTable;
import data.pplTransition.AtomicChange;
import data.pplTransition.PPLTransition;
import data.pplTransition.TableChange;
import dataProccessing.DiffFromTwoSchemas;
import dataProccessing.Worker;

public class GlobalDataKeeper {

	private TreeMap<String,PPLSchema> allPPLSchemas = new TreeMap<String,PPLSchema>();
	private TreeMap<String,PPLTable> allTables = new  TreeMap<String,PPLTable>();
	private ArrayList<AtomicChange> atomicChanges = new ArrayList<AtomicChange>();
	private TreeMap<String,TableChange> tableChanges = new TreeMap<String,TableChange>();
	private TreeMap<String,TableChange> tableChangesForTwo = new TreeMap<String,TableChange>();
	private TreeMap<String,PPLTransition> allPPLTransitions = new TreeMap<String,PPLTransition>();
	private String 	projectDataFolder=null;
	private String filename=null;
	private String transitionsFile="";
	private String oldVersion=null;
	private String newVersion=null;
	

	public GlobalDataKeeper(String fl,String transitionsFile){
		
		filename=fl;
		this.transitionsFile=transitionsFile;
	}
	
//	public GlobalDataKeeper(TreeMap<String,PPLSchema> tmpAllPPLSchemas,TreeMap<String,PPLTable> tmpAllTables,ArrayList<AtomicChange> tmpAtomicChanges,
//														TreeMap<String,TableChange> tmpTableChanges,TreeMap<String,PPLTransition> tmpAllPPLTransitions,String tmpFilename){
//		
//		allPPLSchemas=tmpAllPPLSchemas;
//		allTables=tmpAllTables;
//		atomicChanges=tmpAtomicChanges;
//		tableChanges=tmpTableChanges;
//		allPPLTransitions=tmpAllPPLTransitions;
//		projectDataFolder=tmpFilename;
//		
//	}
	
	public GlobalDataKeeper(String tmpOldVersion, String tmpNewVersion,String tmpDataFolder){
		
		oldVersion=tmpOldVersion;
		newVersion=tmpNewVersion;
		projectDataFolder=tmpDataFolder;
		
	}
	
	public void setData(){
		
		Worker w = new Worker(filename,transitionsFile);
		try {
			w.work();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setAllPPLSchemas(w.getAllPPLSchemas());
		setAllPPLTables(w.getAllPPLTables());
		setAllPPLTransitions(w.getAllPPLTransitions());
		setAllTableChanges(w.getAllTableChanges());
		setAtomicChanges(w.getAtomicChanges());
		setDataFolder(w.getDataFolder());
		
	}
	
	public void setDataForTwoVersions(){
		
		DiffFromTwoSchemas diffForTwoSch= new DiffFromTwoSchemas(oldVersion, newVersion, projectDataFolder);
		System.out.println(oldVersion+" "+newVersion);
		try {
			diffForTwoSch.findDifferenciesFromTwoSchemas();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setTmpTableChanges(diffForTwoSch.getTableChangesForTwoSchemas());

	}
	
	private void setAllPPLSchemas(TreeMap<String,PPLSchema> tmpAllPPLSchemas){
		
		allPPLSchemas=tmpAllPPLSchemas;
		
	}
	
	private void setAllPPLTables(TreeMap<String,PPLTable> tmpAllTables){
		 allTables=tmpAllTables;

		
	}
	
	private void setAtomicChanges(ArrayList<AtomicChange> tmpAtomicChanges){
		
		 atomicChanges=tmpAtomicChanges;
		
	}
	
	private void setAllTableChanges(TreeMap<String,TableChange> tmpTableChanges){
		
		 tableChanges=tmpTableChanges;
		
	}
	
	private void setTmpTableChanges(TreeMap<String,TableChange> tmpTableChanges){
		
		tableChangesForTwo=tmpTableChanges;
		
	}
	
	private void setAllPPLTransitions(TreeMap<String,PPLTransition> tmpAllPPLTransitions){
		
		 allPPLTransitions=tmpAllPPLTransitions;
		
	}
	
	private void setDataFolder(String tmpProjectDataFolder){
		 projectDataFolder=tmpProjectDataFolder;
	}
	
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
	
	public TreeMap<String,TableChange> getTmpTableChanges(){
		
		return tableChangesForTwo;
		
	}
	
	public TreeMap<String,PPLTransition> getAllPPLTransitions(){
		
		return allPPLTransitions;
		
	}
	
	public String getDataFolder(){
		return projectDataFolder;
	}
	
	
}
