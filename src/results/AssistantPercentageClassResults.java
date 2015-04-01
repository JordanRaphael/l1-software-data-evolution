package results;

import java.util.ArrayList;
import java.util.TreeMap;

import data.pplSqlSchema.PPLSchema;

public class AssistantPercentageClassResults implements Results {
	
	
	private PPLSchema oldSchema=null;
	private PPLSchema newSchema=null;
	private float	percentageInsersions=0;
	private float	percentageUpdates=0;
	private float   percentageDeletions=0;
	
	public AssistantPercentageClassResults(PPLSchema tmpFirstSchema,PPLSchema tmpSecondSchema,
			float tmpPercentageInsersions,float tmpPercentageUpdates,float tmpPercentageDeletions){
		
		oldSchema=tmpFirstSchema;
		newSchema=tmpSecondSchema;
		percentageInsersions=tmpPercentageInsersions;
		percentageUpdates=tmpPercentageUpdates;
		percentageDeletions=tmpPercentageDeletions;
		
		
	}
	
	public PPLSchema getOldSchema(){
		
		return oldSchema;
		
	}
	
	public PPLSchema getNewSchema(){
		
		return newSchema;
		
	}

	public float getInsersionsPercentage(){
		
		return percentageInsersions;
	
	}
	
	public float getUpdatesPercentage(){
		
		return percentageUpdates;
	
	}
	
	public float getDeletionsPercentage(){
		
		return percentageDeletions;
	
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
