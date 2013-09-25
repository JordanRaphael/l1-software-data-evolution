package results;

import java.util.ArrayList;

import sqlSchema.Table;

public class PercentageOfChangesResults implements Results {

	private ArrayList<Table> 	percentageTables=new ArrayList<Table>();
	
	public PercentageOfChangesResults(){
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setResults(ArrayList tmpResults){
		percentageTables=tmpResults;
	}
	
	public ArrayList<Table> getResults(){
		return percentageTables;
	}
	
}
