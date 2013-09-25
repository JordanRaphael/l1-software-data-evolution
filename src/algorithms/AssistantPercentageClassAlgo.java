package algorithms;

import sqlSchema.Schema;

public class AssistantPercentageClassAlgo {
	
	
	private Schema oldSchema=null;
	private Schema newSchema=null;
	private float	percentageInsersions=0;
	private float	percentageUpdates=0;
	private float   percentageDeletions=0;
	
	public AssistantPercentageClassAlgo(Schema tmpFirstSchema,Schema tmpSecondSchema,
			float tmpPercentageInsersions,float tmpPercentageUpdates,float tmpPercentageDeletions){
		
		oldSchema=tmpFirstSchema;
		newSchema=tmpSecondSchema;
		percentageInsersions=tmpPercentageInsersions;
		percentageUpdates=tmpPercentageUpdates;
		percentageDeletions=tmpPercentageDeletions;
		
		
	}
	
	public Schema getOldSchema(){
		
		return oldSchema;
		
	}
	
	public Schema getNewSchema(){
		
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
	
}
