package data.pplTransition;

import java.util.ArrayList;
import java.util.TreeMap;

public class TableChange {
	
	
	private String affectedTable;
	private TreeMap<String,ArrayList<AtomicChange>> atomicChanges = new TreeMap<String,ArrayList<AtomicChange>>();
	private ArrayList<AtomicChange> atomicChangesForOneTransition = new ArrayList<AtomicChange>();
	
	public TableChange(String tmpAffectedTable, TreeMap<String,ArrayList<AtomicChange>> tmpAtomicChanges){
		
		affectedTable = tmpAffectedTable;
		atomicChanges = tmpAtomicChanges;
		
	}
	
	public TableChange(){
		
	}
	
	public TableChange(String tmpAffectedTable,ArrayList<AtomicChange> tmpAtomicChanges){
		
		affectedTable = tmpAffectedTable;
		atomicChangesForOneTransition = tmpAtomicChanges;
		
	}
	
	public TreeMap<String,ArrayList<AtomicChange>> getTableAtomicChanges(){
		return atomicChanges;
	}
	
	public ArrayList<AtomicChange> getTableAtChForOneTransition(String transition){
		
		return atomicChanges.get(transition);
		
	}
	
	public ArrayList<AtomicChange> getTableAtChForOneTransition(){
		
		return atomicChangesForOneTransition;
		
	}
	
	
	public String toString(){
		
		String message = "Table Change \n";
		
		for(int i=0; i<atomicChanges.size(); i++){
			
			message=message+atomicChanges.get(i).toString();
			
			message=message+"\n";
			
		}
		
		return message;
		
		
	}
	
	public String getAffectedTableName(){
		
		return affectedTable;
		
	}
	
	

}
