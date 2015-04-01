package results;

import java.util.ArrayList;
import java.util.TreeMap;

import data.pplSqlSchema.PPLTable;

public class IntensiveTablesFromTwoSchemasResults implements Results {
	
	private ArrayList<PPLTable> mostIntensiveTables=new ArrayList<PPLTable>();
	
	public IntensiveTablesFromTwoSchemasResults(){
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setResults(ArrayList tmpResults){
		
		mostIntensiveTables=tmpResults;
	
	}
	
	public ArrayList<PPLTable> getResults(){
		return mostIntensiveTables;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setResults(TreeMap tmpResults) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public TreeMap getResults(String lala) {
		// TODO Auto-generated method stub
		return null;
	}

	

	

}
