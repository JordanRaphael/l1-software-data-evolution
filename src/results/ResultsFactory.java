package results;

public class ResultsFactory {

	private String resultToReturn=null;
	
	public ResultsFactory(String tmpResultToReturn){
		
		resultToReturn=tmpResultToReturn;
		
	}
	
	public Results createResult(){
		
		if(resultToReturn.equals("IntensiveTablesFromTwoSchemaResults")){
			return new IntensiveTablesFromTwoSchemasResults();
		}
		else if(resultToReturn.equals("LongLivedTablesResults")){
			return new LongLivedTablesResults();
		}
		else if(resultToReturn.equals("MostUpdatedAttributesResults")){
			return new MostUpdatedAttributesResults();
		}
		else if(resultToReturn.equals("MostUpdatedTablesResults")){
			return new MostUpdatedTablesResults();
		}
		else{
			return new PercentageOfChangesResults();
		}
		
	}
	
	
}
