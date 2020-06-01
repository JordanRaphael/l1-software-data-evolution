package data.dataKeeper;

import java.util.ArrayList;
import java.util.TreeMap;

import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.TableChange;

public class TableChangeManager {

	private ArrayList<AtomicChange> atomicChanges = null;
	private TreeMap<String, TableChange> tableChanges = null;
	private TreeMap<String, TableChange> tableChangesForTwo = null;
	
	protected TableChangeManager() {
		atomicChanges = new ArrayList<AtomicChange>();
		tableChanges = new TreeMap<String, TableChange>();
		tableChangesForTwo = new TreeMap<String, TableChange>();
	}
	
	protected void setAtomicChanges(ArrayList<AtomicChange> atomicChanges){
		this.atomicChanges = atomicChanges;
	}
	
	protected ArrayList<AtomicChange> getAtomicChanges(){
		return atomicChanges;
	}
	
	protected void setTableChanges(TreeMap<String, TableChange> tableChanges){
		this.tableChanges = tableChanges;
	}
	
	protected TreeMap<String, TableChange> getTableChanges(){
		return tableChanges;
	}
	
	protected void setTableChangesForTwo(TreeMap<String, TableChange> tableChangesForTwo){
		this.tableChangesForTwo = tableChangesForTwo;
	}
	
	protected TreeMap<String, TableChange> getTableChangesForTwo(){
		return tableChangesForTwo;
	}
}
