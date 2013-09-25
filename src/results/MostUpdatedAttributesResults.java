package results;

import java.util.ArrayList;

import sqlSchema.Attribute;

public class MostUpdatedAttributesResults implements Results {
	
	private ArrayList<Attribute> mostUpdatedAttributes=new ArrayList<Attribute>();
	
	public MostUpdatedAttributesResults(){
		
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	
	public void setResults(ArrayList tmpResults) {
		mostUpdatedAttributes=tmpResults;
		
	}

	
	public ArrayList<Attribute> getResults() {
		
		return mostUpdatedAttributes;
	}

}
