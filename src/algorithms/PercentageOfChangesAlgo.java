package algorithms;

import java.util.ArrayList;

import results.PercentageOfChangesResults;
import results.Results;
import sqlSchema.Schema;
import sqlSchema.Table;
import transitions.Deletion;
import transitions.Insersion;
import transitions.Transition;
import transitions.TransitionList;
import transitions.Update;

public class PercentageOfChangesAlgo implements Algorithm {
	
	private ArrayList<TransitionList> transitions=new ArrayList<TransitionList>();
	private ArrayList<Schema> schemas=new ArrayList<Schema>();
	private Results results=null;
	
	public PercentageOfChangesAlgo(ArrayList<Schema> tmpSchemas,ArrayList<TransitionList> tmpTransitions){
		
		schemas=tmpSchemas;
		transitions=tmpTransitions;
		
	}
	
	public Results compute(){
		ArrayList<Table> 	tables=new ArrayList<Table>();
		ArrayList<AssistantPercentageClassAlgo>	percentages=new ArrayList<AssistantPercentageClassAlgo>();
		ArrayList<String>	allTables=new ArrayList<String>();
		int found=0;
		
	
		
		for(int i=0; i<schemas.size(); i++){
			
			Schema oneSchema=schemas.get(i);
			
			
			for(int j=0; j<oneSchema.getTables().size(); j++){
				
				Table oneTable=oneSchema.getTableAt(j);
				
				String tmpTableName=oneTable.getName();
				
				
				
				for(int k=0; k<allTables.size(); k++){
					
					
					if(!tmpTableName.equals(allTables.get(k))){
						found=0;
						
					}
					else{
						found=1;
						break;
						
					}
					
				}
				
				if(found==0){
					if(i>0){
						
						allTables.add(tmpTableName);
						percentages=calculatePercentagesOfOneTable(oneTable,i);
						oneTable.setTransitions(percentages);
						tables.add(oneTable);
						
						oneTable=new Table();
						percentages=new ArrayList<AssistantPercentageClassAlgo>();
					
					}
					else if(i==0 && !(schemas.get(i+1).getTables().containsKey(oneTable.getName()))){
						
						allTables.add(tmpTableName);
						
						percentages=calculatePercentagesOfOneTable(oneTable,i+1);
						oneTable.setTransitions(percentages);
						tables.add(oneTable);
						oneTable=new Table();
						percentages=new ArrayList<AssistantPercentageClassAlgo>();
					}
				}
				else{
					found=0;
				}
				
				
			}
		}
				
		
		results=new PercentageOfChangesResults();
		results.setResults(tables);
		
		return results;
		
		
	}
	
	private ArrayList<AssistantPercentageClassAlgo> calculatePercentagesOfOneTable(Table oneTable,int schemaId){
		
		ArrayList<AssistantPercentageClassAlgo> percentagesUnderConstructions=new ArrayList<AssistantPercentageClassAlgo>();
		int updn=0;
		int deln=0;
		int insn=0;
		int insersionsBetweenVersions=0;
		int updatesBetweenVersions=0;
		int deletionsBetweenVersions=0;
		int previousVersionAttributes=0;
		Schema oldSchema=null;
		Schema newSchema=null;
		
		
			for(int i=0; i<transitions.size(); i++){
				
				TransitionList  tmpTL=transitions.get(i);
				
				ArrayList<Transition> tmpTR=tmpTL.getList();
				
				updn=0;
				deln=0;
				insn=0;
				
				if(tmpTR!=null){
					for(int j=0; j<tmpTR.size(); j++){
						
						 if(tmpTR.get(j) instanceof Deletion){
							
							 Deletion d=(Deletion) tmpTR.get(j);
							 if(d.getAffTable().getName().equals(oneTable.getName())){
								 
								deln=d.getNumOfAffAttributes();
								 
								 
							 }
							 
						 }
						 
						 if(tmpTR.get(j) instanceof Insersion){
							
							 Insersion ins=(Insersion) tmpTR.get(j);
							 
							 if(ins.getAffTable().getName().equals(oneTable.getName())){
								
								insn=ins.getNumOfAffAttributes(); 
								
							 }
							 
							 
						 }
						 
						 if(tmpTR.get(j) instanceof Update){
								
							 Update up=(Update) tmpTR.get(j);
							 
							 if(up.getAffTable().getName().equals(oneTable.getName())){
								 
								updn=up.getNumOfAffAttributes();
								
							 }
							 
							 
						 }
						 
						 
					}
				}
				
				insersionsBetweenVersions=insersionsBetweenVersions+insn;
				updatesBetweenVersions=updatesBetweenVersions+updn;
				deletionsBetweenVersions=deletionsBetweenVersions+deln;
				
				
				
				for(int k=0;k<schemas.size();k++){
					if(tmpTL.getOldVersion().equals(schemas.get(k).getName())){
						oldSchema=schemas.get(k);
						break;
					}
				}
				
				for(int k=0;k<schemas.size();k++){
					if(tmpTL.getNewVersion().equals(schemas.get(k).getName())){
						newSchema=schemas.get(k);
						schemaId=k;
					}
				}
					
					
				for(int l=0;l<schemas.get(schemaId-1).getTables().size();l++){
						
						if(oneTable.getName().equals(schemas.get(schemaId-1).getTableAt(l).getName())){
							
							previousVersionAttributes=schemas.get(schemaId-1).getTableAt(l).getAttrs().size();
								
							break;
						}
					
				}		
							
				float tmpPercentageInsersions;
				float tmpPercentageUpdates;
				float tmpPercentageDeletions;
				if(previousVersionAttributes==0){
					tmpPercentageInsersions=0;
					tmpPercentageUpdates=0;
					tmpPercentageDeletions=0;
				}
				else{
					tmpPercentageInsersions=(float)insersionsBetweenVersions/previousVersionAttributes;
					tmpPercentageUpdates=(float)updatesBetweenVersions/previousVersionAttributes;
					tmpPercentageDeletions=(float)deletionsBetweenVersions/previousVersionAttributes;
				}
				
				AssistantPercentageClassAlgo tmpAssistantPercentageClass=new AssistantPercentageClassAlgo(oldSchema,newSchema,
				tmpPercentageInsersions,tmpPercentageUpdates,tmpPercentageDeletions);
							
				percentagesUnderConstructions.add(tmpAssistantPercentageClass);
							
				tmpAssistantPercentageClass=null;
							
							
						
				insn=0;
				updn=0;
				deln=0;
				insersionsBetweenVersions=0;
				updatesBetweenVersions=0;
				deletionsBetweenVersions=0;
				
			}
		
	
		return percentagesUnderConstructions;
		
		
	}
	

}
