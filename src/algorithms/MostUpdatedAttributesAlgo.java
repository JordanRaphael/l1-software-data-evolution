package algorithms;

import java.util.ArrayList;

import results.MostUpdatedAttributesResults;
import results.Results;
import sqlSchema.Attribute;
import sqlSchema.Schema;
import sqlSchema.Table;
import transitions.Transition;
import transitions.TransitionList;

public class MostUpdatedAttributesAlgo implements Algorithm{
	
	private static ArrayList<Schema> AllSchemas=new ArrayList<Schema>();
	private ArrayList<TransitionList> AllTransitions=new ArrayList<TransitionList>();
	private int k=0;
	private Results results=null;
	
	
	public MostUpdatedAttributesAlgo(ArrayList<Schema> tmpAllSchemas,
			ArrayList<TransitionList> tmpAllTransitions , int tmpk){
		
		AllSchemas=tmpAllSchemas;
		AllTransitions=tmpAllTransitions;
		k=tmpk;
		
		
	}
	
	
	public Results compute(){
		
		ArrayList<Attribute> mostUpdatedAttributes=new ArrayList<Attribute>();
		ArrayList<String> attributeName=new ArrayList<String>();
		
		int found=0;
		int attributeTotalChanges=0;
		
		for(int i=0; i<AllSchemas.size(); i++){
			
			Schema oneSchema=AllSchemas.get(i);
			
			for(int j=0; j<oneSchema.getTables().size(); j++){
				
				Table currentTable=oneSchema.getTableAt(j);
				
				for(int k=0; k<currentTable.getAttrs().size(); k++){
					
					Attribute currentAttribute=currentTable.getAttrAt(k);
					
					for(int l=0; l<attributeName.size(); l++){
						
						if(currentAttribute.getName().equals(attributeName.get(l))){
							found=1;
							break;
						}
						
						
					}
					
					if(found==1){
						found=0;
						currentAttribute=new Attribute();
					}
					else{
						
						attributeTotalChanges=calculateAttributeTotalChanges(currentAttribute);
						currentAttribute.setTotalAttributeChanges(attributeTotalChanges);
						mostUpdatedAttributes.add(currentAttribute);
						attributeName.add(currentAttribute.getName());
						currentAttribute=new Attribute();
						attributeTotalChanges=0;
						
						
					}
					
					
				}
				
				currentTable=new Table();
				
			}
			
		}
		
		mostUpdatedAttributes=sortAttributesByTotalChanges(mostUpdatedAttributes);
		
		for(int i=mostUpdatedAttributes.size()-1; i>=k; i--){
			mostUpdatedAttributes.remove(i);
		}
		
		
		results=new MostUpdatedAttributesResults();
		results.setResults(mostUpdatedAttributes);
		
		return results;
		
		
	}
	
	private int calculateAttributeTotalChanges(Attribute currentAttribute){
		
		int assistantTotalChanges=0;
		
		for(int i=0;i<AllTransitions.size(); i++){
			
			TransitionList  tmpTL=AllTransitions.get(i);
			
			ArrayList<Transition> tmpTR=tmpTL.getList();
			if(tmpTR!=null){
				for(int j=0; j<tmpTR.size(); j++){
					
					Transition tr=tmpTR.get(j);
					ArrayList<Attribute> affectedAttributes=(ArrayList<Attribute>) tr.getAffAttributes();
				
					for(int l=0; l<affectedAttributes.size(); l++){
						
						if(affectedAttributes.get(l).getName().equals(currentAttribute.getName())){
							assistantTotalChanges++;
						}
						
					}
					
					tr=null;
					affectedAttributes=new ArrayList<Attribute>();
					
				}
			
			}
			
		}
		
		return assistantTotalChanges;
		
		
	}
	
	private ArrayList<Attribute> sortAttributesByTotalChanges(ArrayList<Attribute> tmpMostUpdatedAttributes){
		ArrayList<Attribute> sortingTables=new ArrayList<Attribute>();
		
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
	

}
