package algorithms;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.pplSqlSchema.PPLAttribute;
import data.pplSqlSchema.PPLSchema;
import data.pplSqlSchema.PPLTable;
import data.pplTransition.AtomicChange;
import data.pplTransition.PPLTransition;
import data.pplTransition.TableChange;
import results.Results;
import results.ResultsFactory;

public class MostUpdatedAttributesAlgo implements Algorithm{
	
	private static TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private TreeMap<String,PPLTransition> allPPLTransitions=new TreeMap<String,PPLTransition>();
	private ArrayList<PPLAttribute> mostUpdatedAttributes=new ArrayList<PPLAttribute>();
	private int k=0;
	private Results results=null;
	
	
	public MostUpdatedAttributesAlgo(TreeMap<String,PPLSchema> tmpAllSchemas,
			TreeMap<String,PPLTransition> tmpAllTransitions , int tmpk){
		allPPLSchemas=tmpAllSchemas;
		allPPLTransitions=tmpAllTransitions;
		k=tmpk;
		
	}

//	public void setAll(TreeMap<String,PPLSchema> tmpAllSchemas,
//			TreeMap<String,PPLTransition> tmpAllTransitions , int tmpk){
//		
//		allPPLSchemas=tmpAllSchemas;
//		allPPLTransitions=tmpAllTransitions;
//		k=tmpk;
//		
//	}
	
	public Results compute(){
		
		ArrayList<String> attributeName=new ArrayList<String>();
		
		int found=0;
		int attributeTotalChanges=0;
		
		for (Map.Entry<String, PPLSchema> pplSc : allPPLSchemas.entrySet()) {
			
			PPLSchema oneSchema=pplSc.getValue();
			
			for(int j=0; j<oneSchema.getTables().size(); j++){
				
				PPLTable currentTable=oneSchema.getTableAt(j);
				
				for(int k=0; k<currentTable.getAttrs().size(); k++){
					
					PPLAttribute currentAttribute=currentTable.getAttrAt(k);
					
					for(int l=0; l<attributeName.size(); l++){
						
						if(currentAttribute.getName().equals(attributeName.get(l))){
							found=1;
							break;
						}
						
						
					}
					
					if(found==1){
						found=0;
						currentAttribute=new PPLAttribute();
					}
					else{
						
						attributeTotalChanges=calculateAttributeTotalChanges(currentAttribute);
						currentAttribute.setTotalAttributeChanges(attributeTotalChanges);
						mostUpdatedAttributes.add(currentAttribute);
						attributeName.add(currentAttribute.getName());
						currentAttribute=new PPLAttribute();
						attributeTotalChanges=0;
						
						
					}
					
					
				}
				
				currentTable=new PPLTable();
				
			}
			
		}
		
		mostUpdatedAttributes=sortAttributesByTotalChanges(mostUpdatedAttributes);
		
		for(int i=mostUpdatedAttributes.size()-1; i>=k; i--){
			mostUpdatedAttributes.remove(i);
		}
		
		
		ResultsFactory rf = new ResultsFactory("MostUpdatedAttributesResults");
		results=rf.createResult();
		results.setResults(mostUpdatedAttributes);
		
		return results;
		
		
	}
	
	private int calculateAttributeTotalChanges(PPLAttribute currentAttribute){
		
		int assistantTotalChanges=0;
		
		for(Map.Entry<String, PPLTransition> pplTr:allPPLTransitions.entrySet()){
			
			PPLTransition  tmpTL=pplTr.getValue();
			ArrayList<TableChange> tmpTR=tmpTL.getTableChanges();

			if(tmpTR!=null){
				for(int j=0; j<tmpTR.size(); j++){
					
					TableChange tC=tmpTR.get(j);
					ArrayList<AtomicChange> aCs=tC.getTableAtChForOneTransition();
					if(aCs!=null){

						for(int l=0; l<aCs.size(); l++){
							if(aCs.get(l).getAffectedAttrName().equals(currentAttribute.getName())){
								assistantTotalChanges++;
							}
							
						}
					}
					
				}
			
			}
			
		}
		
		return assistantTotalChanges;
		
		
	}
	
	private ArrayList<PPLAttribute> sortAttributesByTotalChanges(ArrayList<PPLAttribute> tmpMostUpdatedAttributes){
		ArrayList<PPLAttribute> sortingTables=new ArrayList<PPLAttribute>();
		
		for(int i=0; i<tmpMostUpdatedAttributes.size(); i++){
			
			if(i==0){
				
				sortingTables.add(tmpMostUpdatedAttributes.get(i));
			
			}
			else{
				
				for(int j=0; j<sortingTables.size(); j++){
					
					if(tmpMostUpdatedAttributes.get(i).getTotalAttributeChanges()>sortingTables.get(j).getTotalAttributeChanges()){
						sortingTables.add(j, tmpMostUpdatedAttributes.get(i));
						break;
					}
					else if(j==sortingTables.size()-1){
						sortingTables.add(tmpMostUpdatedAttributes.get(i));
						break;
					}
					
				}
				
				
			}
			
		}
		
		return sortingTables;
	}


	@Override
	public void compute(String compute) {
		// TODO Auto-generated method stub
		
	}


	

}
