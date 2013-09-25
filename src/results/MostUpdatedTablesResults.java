package results;

import java.util.ArrayList;

import sqlSchema.Table;

public class MostUpdatedTablesResults implements Results {

	private ArrayList<Table> mostUpdatedTables=new ArrayList<Table>();
	
	public MostUpdatedTablesResults(){
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setResults(ArrayList tmpResults){
		mostUpdatedTables=tmpResults;
	}
	
	public ArrayList<Table> getResults(){
		return mostUpdatedTables;
	}
	
}
