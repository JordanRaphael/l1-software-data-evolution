package algorithms;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import data.pplSqlSchema.PPLSchema;
import data.pplSqlSchema.PPLTable;
import data.pplTransition.AtomicChange;
import data.pplTransition.PPLTransition;
import data.pplTransition.TableChange;
import results.AssistantPercentageClassResults;
import results.Results;
import results.ResultsFactory;

public class PercentageOfChangesAlgo implements Algorithm {
	
	private TreeMap<String,PPLTransition> transitions=new TreeMap<String,PPLTransition>();
	private TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private TreeMap <String,ArrayList<AssistantPercentageClassResults>> percentagesSndApproach = new TreeMap<String,ArrayList<AssistantPercentageClassResults>>();

	private Results results=null;
	
	public PercentageOfChangesAlgo(TreeMap<String,PPLSchema> tmpSchemas, TreeMap<String,PPLTransition> tmpTransitions){
		allPPLSchemas=tmpSchemas;
		transitions=tmpTransitions;
		
	}
	
//	public void setAll(TreeMap<String,PPLSchema> tmpSchemas, TreeMap<String,PPLTransition> tmpTransitions){
//		allPPLSchemas=tmpSchemas;
//		transitions=tmpTransitions;
//		
//	}
	
	public Results compute(){
		new ArrayList<PPLTable>();
		ArrayList<AssistantPercentageClassResults>	percentages=new ArrayList<AssistantPercentageClassResults>();
		
		
		Set<String> allPPLSchNames = allPPLSchemas.keySet();
		ArrayList<String> allPPLSchNamesA = new ArrayList<String>();
		
		for(String name: allPPLSchNames){
			allPPLSchNamesA.add(name);
		}
		
		
		for(int i=0; i<allPPLSchNamesA.size(); i++){
			
			PPLSchema oneSchema=allPPLSchemas.get(allPPLSchNamesA.get(i));
			
			for(int j=0; j<oneSchema.getTables().size(); j++){
				
				PPLTable oneTable=oneSchema.getTableAt(j);
			
				percentages=calculatePercentagesOfOneTable(oneTable,i);
				percentagesSndApproach.put(oneTable.getName(), percentages);
				
				oneTable=new PPLTable();
				percentages=new ArrayList<AssistantPercentageClassResults>();
			}
		}
				
		

		ResultsFactory rf = new ResultsFactory("");
		results=rf.createResult();
		results.setResults(percentagesSndApproach);
		
		return results;
		
		
	}
	
	private ArrayList<AssistantPercentageClassResults> calculatePercentagesOfOneTable(PPLTable oneTable,int schemaId){
		
		ArrayList<AssistantPercentageClassResults> percentagesUnderConstructions=new ArrayList<AssistantPercentageClassResults>();
		int updn=0;
		int deln=0;
		int insn=0;
		int insersionsBetweenVersions=0;
		int updatesBetweenVersions=0;
		int deletionsBetweenVersions=0;
		int previousVersionAttributes=0;
		PPLSchema oldSchema=null;
		PPLSchema newSchema=null;
		
		for(Map.Entry<String, PPLTransition> pplTr:transitions.entrySet()){
			
			PPLTransition  tmpTL=pplTr.getValue();
			ArrayList<TableChange> tmpTR=tmpTL.getTableChanges();
			
			updn=0;
			deln=0;
			insn=0;
			
			if(tmpTR!=null){
				
				for(int j=0; j<tmpTR.size(); j++){
					
					TableChange tC=tmpTR.get(j);
					
					ArrayList<AtomicChange> atChs = tC.getTableAtChForOneTransition();
					//System.out.println(atChs.size());
					for(int k=0; k<atChs.size(); k++){
						
						if(atChs.get(k).getType().contains("Deletion") && atChs.get(k).getAffectedTableName().equals(oneTable.getName())){
							deln++;
							//System.out.println("Type "+atChs.get(k).getType()+" Table "+atChs.get(k).getAffectedAttrName());
						}
						else if(atChs.get(k).getType().contains("Addition") && atChs.get(k).getAffectedTableName().equals(oneTable.getName())){
							insn++;
						}
						else if(atChs.get(k).getType().contains("Change") && atChs.get(k).getAffectedTableName().equals(oneTable.getName())){
							updn++;
						}
					}
							
					/* if(tmpTR.get(j) instanceof Deletion){
						
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
						 
						 
					 }*/
					 

				}

				System.out.println(oneTable.getName()+" "+deln+" "+insn+" "+updn);

			}
			
			
			insersionsBetweenVersions=insersionsBetweenVersions+insn;
			updatesBetweenVersions=updatesBetweenVersions+updn;
			deletionsBetweenVersions=deletionsBetweenVersions+deln;
			
			
			
			for (Map.Entry<String, PPLSchema> pplSch : allPPLSchemas.entrySet()) {
				if(tmpTL.getOldVersionName().equals(pplSch.getValue().getName())){
					oldSchema=pplSch.getValue();
					break;
				}
			}
			
			int k=0;
			for (Map.Entry<String, PPLSchema> pplSch : allPPLSchemas.entrySet()) {
				if(tmpTL.getNewVersionName().equals(pplSch.getValue().getName())){
					newSchema=pplSch.getValue();
					schemaId=k;
				}
				k++;
			}
			
			System.out.println(schemaId);
				 
			Set<String> allPPLNames = allPPLSchemas.keySet();
			ArrayList<String> allPPLNamesA = new ArrayList<String>();
				
			for(String name:allPPLNames){
				allPPLNamesA.add(name);
			}
			
			for(int l=0;l<allPPLSchemas.get(allPPLNamesA.get(schemaId-1)).getTables().size();l++){
					
					if(oneTable.getName().equals(allPPLSchemas.get(allPPLNamesA.get(schemaId-1)).getTableAt(l).getName())){
						
						previousVersionAttributes=allPPLSchemas.get(allPPLNamesA.get(schemaId-1)).getTableAt(l).getAttrs().size();
							
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
			
			AssistantPercentageClassResults tmpAssistantPercentageClass=new AssistantPercentageClassResults(oldSchema,newSchema,
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

	@Override
	public void compute(String compute) {
		// TODO Auto-generated method stub
		
	}

	

}
