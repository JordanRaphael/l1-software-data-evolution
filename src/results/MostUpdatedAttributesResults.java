package results;

import java.util.ArrayList;
import java.util.TreeMap;

import data.pplSqlSchema.PPLAttribute;

public class MostUpdatedAttributesResults implements Results {
	
	private ArrayList<PPLAttribute> mostUpdatedAttributes=new ArrayList<PPLAttribute>();
	
	public MostUpdatedAttributesResults(){
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setResults(ArrayList tmpResults) {
		mostUpdatedAttributes=tmpResults;
		
	}

	
	public ArrayList<PPLAttribute> getResults() {
		
		return mostUpdatedAttributes;
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
