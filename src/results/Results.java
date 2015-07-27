package results;

import java.util.ArrayList;
import java.util.TreeMap;

public interface Results {

	@SuppressWarnings("rawtypes")
	public void setResults(ArrayList tmpResults);
	@SuppressWarnings("rawtypes")
	public ArrayList getResults();
	
	@SuppressWarnings("rawtypes")
	public void setResults(TreeMap tmpResults);
	@SuppressWarnings("rawtypes")
	public TreeMap getResults(String lala);
	
	
}
