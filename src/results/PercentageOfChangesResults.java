package results;

import java.util.ArrayList;
import java.util.TreeMap;

public class PercentageOfChangesResults implements Results {

	private TreeMap<String,ArrayList<AssistantPercentageClassResults>>	percentageTables=new TreeMap<String,ArrayList<AssistantPercentageClassResults>>();
	
	public PercentageOfChangesResults(){
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setResults(ArrayList tmpResults) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ArrayList getResults() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setResults(TreeMap tmpResults) {

		percentageTables=tmpResults;
		
	}

	@Override
	public TreeMap<String, ArrayList<AssistantPercentageClassResults>> getResults(String lala) {
		// TODO Auto-generated method stub
		return	percentageTables;
	}

	

	
}
