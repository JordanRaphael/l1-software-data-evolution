package results;

import java.util.ArrayList;
import java.util.TreeMap;

import data.pplSqlSchema.PPLTable;

public class MostUpdatedTablesResults implements Results {

	private ArrayList<PPLTable> mostUpdatedTables=new ArrayList<PPLTable>();
	
	public MostUpdatedTablesResults(){
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setResults(ArrayList tmpResults){
		mostUpdatedTables=tmpResults;
	}
	
	public ArrayList<PPLTable> getResults(){
		return mostUpdatedTables;
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
