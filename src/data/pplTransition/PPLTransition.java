package data.pplTransition;

import java.util.ArrayList;

public class PPLTransition {
	
	
	private String oldSchema;
	private String newSchema;
	
	private ArrayList<TableChange> tableChanges = new ArrayList<TableChange>();
	
	public PPLTransition(String tmpOldSchema, String tmpNewSchema){
		
		oldSchema = tmpOldSchema;
		
		newSchema = tmpNewSchema;
		
	}
	
	public void setTableChanges(ArrayList<TableChange> tmpTableChanges){
		
		tableChanges = tmpTableChanges;
		
	}
	
	public ArrayList<TableChange> getTableChanges(){
		
		return tableChanges;
		
	}
	
	public String getNewVersionName(){
		
		return newSchema;
		
	}
	
	public String getOldVersionName(){
		
		return oldSchema;
		
	}
	
	
}
