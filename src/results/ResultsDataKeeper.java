package results;

import java.util.ArrayList;
import java.util.TreeMap;

import data.pplSqlSchema.PPLAttribute;
import data.pplSqlSchema.PPLTable;

public class ResultsDataKeeper {
	
	private ArrayList<PPLTable> longLivedTables=new ArrayList<PPLTable>();
	private ArrayList<PPLTable> mostUpdatedTables=new ArrayList<PPLTable>();
	private ArrayList<PPLTable> mostIntensiveTables=new ArrayList<PPLTable>();
	private ArrayList<PPLAttribute> mostUpdatedAttributes=new ArrayList<PPLAttribute>();
	private TreeMap<String,ArrayList<AssistantPercentageClassResults>> percentageOfChangesAboutTables=new TreeMap<String,ArrayList<AssistantPercentageClassResults>>();
	
	public void setLongLivedTables(ArrayList<PPLTable> tmpLoLiTables){
		
		longLivedTables=tmpLoLiTables;
	}
	
	public void setMostUpdatedTables(ArrayList<PPLTable> tmpMostUpdatedTables){
		
		mostUpdatedTables=tmpMostUpdatedTables;
	}
	
	public void setMostIntensiveTables(ArrayList<PPLTable> tmpMostIntensiveTables){
		
		mostIntensiveTables=tmpMostIntensiveTables;
	}

	public void setMostUpdatedAttributes(ArrayList<PPLAttribute> tmpMostUpdatedAttributes){
		
		mostUpdatedAttributes=tmpMostUpdatedAttributes;
	}
	
	public void setPercentageOfChangesAboutTables(TreeMap<String,ArrayList<AssistantPercentageClassResults>> tmpPercentageOfChanges){
		
		percentageOfChangesAboutTables = tmpPercentageOfChanges;
	}
	
	public ArrayList<PPLTable> getLongLivedTables(){
		
		return longLivedTables;
	}
	
	public ArrayList<PPLTable> getMostUpdatedTables(){
		
		return mostUpdatedTables;
	}
	
	public ArrayList<PPLTable> getMostIntensiveTables(){
		
		return mostIntensiveTables;
	}

	public ArrayList<PPLAttribute> getMostUpdatedAttributes(){
		
		return mostUpdatedAttributes;
	}
	
	public TreeMap<String,ArrayList<AssistantPercentageClassResults>> getPercentageOfChangesAboutTables(){
		
		return percentageOfChangesAboutTables;
	}
	
}
