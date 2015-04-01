package results;

import java.util.ArrayList;
import java.util.TreeMap;

import data.pplSqlSchema.PPLTable;

public class LongLivedTablesResults implements Results {
	
	private ArrayList<PPLTable> longLivedTables=new ArrayList<PPLTable>();
	
	public LongLivedTablesResults(){
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setResults(ArrayList tmpResults){
		longLivedTables=tmpResults;
	}
	
	public ArrayList<PPLTable> getResults(){
		return longLivedTables;
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
