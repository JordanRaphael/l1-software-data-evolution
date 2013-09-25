package results;

import java.util.ArrayList;

import sqlSchema.Table;

public class LongLivedTablesResults implements Results {
	
	private ArrayList<Table> longLivedTables=new ArrayList<Table>();
	
	public LongLivedTablesResults(){
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setResults(ArrayList tmpResults){
		longLivedTables=tmpResults;
	}
	
	public ArrayList<Table> getResults(){
		return longLivedTables;
	}

}
