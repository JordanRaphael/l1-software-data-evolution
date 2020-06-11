package data.dataKeeper;

import java.util.ArrayList;
import java.util.TreeMap;

import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.TableChange;

public class TableChangeManager {

	private ArrayList<AtomicChange> atomicChanges;
	private TreeMap<String, TableChange> tableChanges;
	private TreeMap<String, TableChange> tableChangesForTwo;
	
	public TableChangeManager() {
		
		atomicChanges = new ArrayList<AtomicChange>();
		tableChanges = new TreeMap<String, TableChange>();
		tableChangesForTwo = new TreeMap<String, TableChange>();
	}
	public ArrayList<AtomicChange> getAtomicChanges(){
		
		return atomicChanges;
	}
	
	public void setAtomicChanges(ArrayList<AtomicChange> atomicChanges){
		
		this.atomicChanges = atomicChanges;
	}
	
	
	public void setTableChanges(TreeMap<String, TableChange> tableChanges){
		
		this.tableChanges = tableChanges;
	}
	
	
	public TreeMap<String, TableChange> getTableChanges(){
	
		return tableChanges;
	}
	
	
	public void setTableChangesForTwo(TreeMap<String, TableChange> tableChangesForTwo){
	
		this.tableChangesForTwo = tableChangesForTwo;
	}
	
	
	public TreeMap<String, TableChange> getTableChangesForTwo(){
	
		return tableChangesForTwo;
	}
	
	public void setAllTableChanges(TreeMap<String, TableChange> tableChanges) {

		this.tableChanges = tableChanges;
	}
}
