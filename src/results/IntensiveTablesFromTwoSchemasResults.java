package results;

import java.util.ArrayList;

import sqlSchema.Table;

public class IntensiveTablesFromTwoSchemasResults implements Results {
	
	private ArrayList<Table> mostIntensiveTables=new ArrayList<Table>();
	
	public IntensiveTablesFromTwoSchemasResults(){
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setResults(ArrayList tmpResults){
		
		mostIntensiveTables=tmpResults;
	
	}
	
	public ArrayList<Table> getResults(){
		return mostIntensiveTables;
	}

}
