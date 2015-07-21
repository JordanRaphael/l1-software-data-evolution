package algorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import data.pplSqlSchema.PPLSchema;
import data.pplSqlSchema.PPLTable;
import data.pplTransition.PPLTransition;
import data.pplTransition.TableChange;
import results.Results;

public class FindCoChanges implements Algorithm {
	
	/***** TransitionList prepei na ginei PPLTransition *****/
	
	@SuppressWarnings("unused")
	private static TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private TreeMap<String,PPLTransition> allPPLTransitions=new TreeMap<String,PPLTransition>();
	private TreeMap<String,PPLTable> allTables=new TreeMap<String,PPLTable>();
	private String project=null;
	
	public FindCoChanges(TreeMap<String,PPLSchema> tmpAllSchemas,
			TreeMap<String,PPLTransition> tmpAllTransitions,TreeMap<String,PPLTable> tmpAllTables,String tmpProject){
		allPPLSchemas=tmpAllSchemas;
		allPPLTransitions=tmpAllTransitions;
		allTables=tmpAllTables;
		project=tmpProject;
		
	}
//
//	public void setAll(TreeMap<String,PPLSchema> tmpAllSchemas,
//			TreeMap<String,PPLTransition> tmpAllTransitions,TreeMap<String,PPLTable> tmpAllTables,String tmpProject){
//
//		allPPLSchemas=tmpAllSchemas;
//		allPPLTransitions=tmpAllTransitions;
//		allTables=tmpAllTables;
//		project=tmpProject;
//		
//	}
//	
	private void computeCoChanges(){
		
		
		
		for (Map.Entry<String,PPLTable> tb : allTables.entrySet()) {
			
			HashMap<String,Integer> tmpCoChanges=new HashMap<String,Integer>();
			
			for (Map.Entry<String,PPLTable> tmpTb : allTables.entrySet()) {

					tmpCoChanges.put(tmpTb.getKey(), 0);
				
			}
			
			tb.getValue().setCoChanges(tmpCoChanges);
			
			allTables.put(tb.getKey(), tb.getValue());
			
			calculateExactNumber(tb.getKey(),tb.getValue());
			
		}
		
		
		for (Map.Entry<String,PPLTable> tmpTb : allTables.entrySet()) {
		
			PPLTable lala=tmpTb.getValue();
	
		    System.out.println( "Who are you? " +lala.getName() );
			for ( String key : lala.getCoChanges().keySet() ) {
						
				if(lala.getCoChanges().get(key)!=0)
						System.out.println( "For Who? "+key+" : "+lala.getCoChanges().get(key));
					    
						
			}
			 //}
			
		}
		
		System.out.println(allPPLTransitions.size());
		
		
		
		
	}
	
//	private void calculateExactNumber(String index,PPLTable oneTable){
//		
//			
//		int found=0;
//		
//		HashMap<String,Integer> tmpCoChanges=new HashMap<String,Integer>();
//
//		
//		tmpCoChanges=oneTable.getCoChanges();
//		
//
//		for(int i=0; i<AllTransitions.size(); i++){
//			
//			TransitionList  tmpTL=AllTransitions.get(i);
//			ArrayList<Transition> tmpTR=tmpTL.getList();
//			
//			
//			
//			if(tmpTR!=null){
//				
//				found=0;
//				
//				for(int j=0; j<tmpTR.size(); j++){
//					
//					 if(tmpTR.get(j) instanceof Deletion){
//						
//						 Deletion d=(Deletion) tmpTR.get(j);
//						 if(d.getAffTable().getName().equals(oneTable.getName())){
//							 
//							 
//							 found=1;
//							 
//							 break;
//							 
//						 }
//						 
//					 }
//					 
//					 if(tmpTR.get(j) instanceof Insersion){
//						
//						 
//						 
//						 Insersion ins=(Insersion) tmpTR.get(j);
//						 
//						 if(ins.getAffTable().getName().equals(oneTable.getName())){
//								
//
//							 found=1;
//
//							 break;
//							 
//						 }
//						 
//						 
//					 }
//					 
//					 if(tmpTR.get(j) instanceof Update){
//							
//						 Update up=(Update) tmpTR.get(j);
//						 
//						 if(up.getAffTable().getName().equals(oneTable.getName())){
//								
//							 found=1;
//							 
//							 break;
//
//						 }
//						 
//						 
//					 }
//					 
//					 
//				}
//				
//				if(found==1){
//					
//					ArrayList<String> tablesInTheSameTransition=new ArrayList<String>();
//					
//					for(int k=0; k<tmpTR.size(); k++){
//						
//						 if(tmpTR.get(k) instanceof Deletion){
//							
//							Deletion d=(Deletion) tmpTR.get(k);
//								 
//							
//							if(!tablesInTheSameTransition.contains(d.getAffTable().getName())){
//								
//								
//								tablesInTheSameTransition.add(d.getAffTable().getName());
//								
//								tmpCoChanges.put(d.getAffTable().getName(),tmpCoChanges.get(d.getAffTable().getName())+1);
//								
//								
//							}
//							
//								 
//						 }
//						 
//						 if(tmpTR.get(k) instanceof Insersion){
//							
//							 
//							 
//							Insersion ins=(Insersion) tmpTR.get(k);
//							 
//							if(!tablesInTheSameTransition.contains(ins.getAffTable().getName())){
//								
//								
//									tablesInTheSameTransition.add(ins.getAffTable().getName());
//									tmpCoChanges.put(ins.getAffTable().getName(),tmpCoChanges.get(ins.getAffTable().getName())+1);
//									
//									
//							}
//							 
//							 
//						 }
//						 
//						 if(tmpTR.get(k) instanceof Update){
//								
//							 Update up=(Update) tmpTR.get(k);
//							 
//							 if(!tablesInTheSameTransition.contains(up.getAffTable().getName())){
//									
//								 
//									tablesInTheSameTransition.add(up.getAffTable().getName());
//									
//									tmpCoChanges.put(up.getAffTable().getName(),tmpCoChanges.get(up.getAffTable().getName())+1);
//									
//									
//							}
//							 
//							
//							 
//						 }
//						 
//						 
//					}
//					
//				}
//				
//				
//			}
//			
//			
//			
//			
//		}
//		
//		allTables.get(index).setCoChanges(tmpCoChanges);
//
//		
//		
//	}
	
	private void calculateExactNumber(String index,PPLTable oneTable){
		
		
		int found=0;
		
		HashMap<String,Integer> tmpCoChanges=new HashMap<String,Integer>();

		
		tmpCoChanges=oneTable.getCoChanges();
		
		System.out.println("23r2dfcsdd! "+allPPLTransitions.size());

		for(Map.Entry<String, PPLTransition> pplTr:allPPLTransitions.entrySet()){
			
			PPLTransition  tmpTL=pplTr.getValue();
			ArrayList<TableChange> tmpTR=tmpTL.getTableChanges();

			if(tmpTR!=null){
				
				found=0;

				for(int i=0; i<tmpTR.size(); i++){
					
					TableChange tablCh = tmpTR.get(i);
					
					if(tablCh.getAffectedTableName().equals(oneTable.getName())){
						found=1;
						 
						break;
					}
					
				}
						
				if(found==1){
					
					ArrayList<String> tablesInTheSameTransition=new ArrayList<String>();
					
					for(int k=0; k<tmpTR.size(); k++){
						
						if(!tablesInTheSameTransition.contains(tmpTR.get(k).getAffectedTableName())){
							
							tablesInTheSameTransition.add(tmpTR.get(k).getAffectedTableName());
							tmpCoChanges.put(tmpTR.get(k).getAffectedTableName(),tmpCoChanges.get(tmpTR.get(k).getAffectedTableName())+1);
							
						}
							
					}
						 
				}
					
			}
				
				
		}
			
		
		allTables.get(index).setCoChanges(tmpCoChanges);

		
		
	}
	
	private void computeSequenceCoChanges(){
		
		
		for (Map.Entry<String,PPLTable> tb : allTables.entrySet()) {
			
			
			HashMap<String,Integer> tmpSequenceCoChanges=new HashMap<String,Integer>();
			
			
			for (Map.Entry<String,PPLTable> tmpTb : allTables.entrySet()) {
				
				tmpSequenceCoChanges.put(tmpTb.getKey(), 0);
				
			}
			
			
			tb.getValue().setSequenceCoChanges(tmpSequenceCoChanges);

			allTables.put(tb.getKey(), tb.getValue());
			
			calculateSequenceExactNumber(tb.getKey(),tb.getValue());

			
			
		}
		
	}
	
	private void calculateSequenceExactNumber(String index,PPLTable oneTable){
		
		
		int found=0;
		
		HashMap<String,Integer> tmpSequenceCoChanges=oneTable.getSequenceCoChanges();
		
		int pos=0;
		
		String[] mapKeys = new String[allPPLTransitions.size()];
		int pos2 = 0;
		for (String key : allPPLTransitions.keySet()) {
		    mapKeys[pos2++] = key;
		}
		
		for(Map.Entry<String, PPLTransition> pplTr:allPPLTransitions.entrySet()){
			
			PPLTransition  tmpTL=pplTr.getValue();
			ArrayList<TableChange> tmpTR=tmpTL.getTableChanges();
			
			
			if(tmpTR!=null){
				
				found=0;
				
				for(int i=0; i<tmpTR.size(); i++){
					
					TableChange tablCh = tmpTR.get(i);
										
					if(tablCh.getAffectedTableName().equals(oneTable.getName())){
						
						found=1;
						 
						break;
					}
					
				}
				
				if(found==1){
					
					ArrayList<String> tablesInTheSameTransition=new ArrayList<String>();
					
					int decision=0;
					
					if((pos+2>=allPPLTransitions.size()) || (pos+1>=allPPLTransitions.size())){
						
						decision=allPPLTransitions.size();
						
					}
					else{
						
						decision=pos+2;
						
					}
					
					for(int k=pos; k<decision; k++){
						
						String pos3=mapKeys[k];

						PPLTransition  tmpTL1=allPPLTransitions.get(pos3);
						ArrayList<TableChange> tmpTR1=tmpTL1.getTableChanges();
						
						if(tmpTR1!=null){
					
							for(int j=0; j<tmpTR1.size(); j++){
										 
								if(!tablesInTheSameTransition.contains(tmpTR1.get(j).getAffectedTableName())){
										
										tablesInTheSameTransition.add(tmpTR1.get(j).getAffectedTableName());
										
										tmpSequenceCoChanges.put(tmpTR1.get(j).getAffectedTableName(),tmpSequenceCoChanges.get(tmpTR1.get(j).getAffectedTableName())+1);
										
								}	 
							}
						}			
					}
				}
			}
			
			pos++;
		}
		
		allTables.get(index).setSequenceCoChanges(tmpSequenceCoChanges);

		
		
	}
	
	private void computeWindowCoChanges(){
		
		
		for (Map.Entry<String,PPLTable> tb : allTables.entrySet()) {
			
			
			HashMap<String,Integer> tmpWindowCoChanges=new HashMap<String,Integer>();
			
			
			for (Map.Entry<String,PPLTable> tmpTb : allTables.entrySet()) {
				
				tmpWindowCoChanges.put(tmpTb.getKey(), 0);
			
			}
			
			
			tb.getValue().setWindowCoChanges(tmpWindowCoChanges);
			
			allTables.put(tb.getKey(), tb.getValue());
			
		}
		
		for (Map.Entry<String,PPLTable> tb : allTables.entrySet()) {

			calculateWindowExactNumber(tb.getKey(),tb.getValue());

		}
	
	}
	
	private void calculateWindowExactNumber(String index,PPLTable oneTable){
		
		
		int found=0;
		
		HashMap<String,Integer> tmpWindowCoChanges=oneTable.getWindowCoChanges();
		
		int pos=0;
		
		String[] mapKeys = new String[allPPLTransitions.size()];
		int pos2 = 0;
		for (String key : allPPLTransitions.keySet()) {
		    mapKeys[pos2++] = key;
		}
		
		for(Map.Entry<String, PPLTransition> pplTr:allPPLTransitions.entrySet()){
			
			PPLTransition  tmpTL=pplTr.getValue();
			ArrayList<TableChange> tmpTR=tmpTL.getTableChanges();
			
			
			if(tmpTR!=null){
				
				found=0;
				
				for(int i=0; i<tmpTR.size(); i++){
					
					TableChange tablCh = tmpTR.get(i);
					
					if(tablCh.getAffectedTableName().equals(oneTable.getName())){
						
						found=1;
						 
						break;
					}
					 
				}
				
				if(found==1){
					
					ArrayList<String> tablesInTheSameTransition=new ArrayList<String>();
					
					int decision=0;
					
					if((pos+2>=allPPLTransitions.size()) || (pos+1>=allPPLTransitions.size())){
						
						decision=allPPLTransitions.size();
						
					}
					else{
						
						decision=pos+2;
						
					}
					
					for(int k=pos; k<decision; k++){
						
						String pos3=mapKeys[k];

						
						PPLTransition  tmpTL1=allPPLTransitions.get(pos3);
						ArrayList<TableChange> tmpTR1=tmpTL1.getTableChanges();
						
						
						if(tmpTR1!=null){
					
							for(int j=0; j<tmpTR1.size(); j++){
									 
								if(!tablesInTheSameTransition.contains(tmpTR1.get(j).getAffectedTableName())){
									
									tablesInTheSameTransition.add(tmpTR1.get(j).getAffectedTableName());
									
									tmpWindowCoChanges.put(tmpTR1.get(j).getAffectedTableName(),tmpWindowCoChanges.get(tmpTR1.get(j).getAffectedTableName())+1);
									
									for (String tb : allTables.keySet()) {
										
										if(tb.equals(tmpTR1.get(j).getAffectedTableName())){
											
											allTables.get(tb).getWindowCoChanges().put(oneTable.getName(), allTables.get(tb).getWindowCoChanges().get(oneTable.getName())+1);
											
										}
										
									}
									
								}
								 
							}
						}			
					}
				}
			}
			pos++;
		}
		
		allTables.get(index).setWindowCoChanges(tmpWindowCoChanges);

		
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
		   List mapKeys = new ArrayList(passedMap.keySet());
		   List mapValues = new ArrayList(passedMap.values());
		   Collections.sort(mapValues);
		   Collections.sort(mapKeys);
		   
		   Collections.reverse(mapValues);
		   Collections.reverse(mapKeys);


		   LinkedHashMap sortedMap = new LinkedHashMap();

		   Iterator valueIt = mapValues.iterator();
		   while (valueIt.hasNext()) {
		       Object val = valueIt.next();
		       Iterator keyIt = mapKeys.iterator();

		       while (keyIt.hasNext()) {
		           Object key = keyIt.next();
		           String comp1 = passedMap.get(key).toString();
		           String comp2 = val.toString();

		           if (comp1.equals(comp2)){
		               passedMap.remove(key);
		               mapKeys.remove(key);
		               sortedMap.put((String)key, (Integer)val);
		               break;
		           }

		       }

		   }
		   return sortedMap;
	}
	
	private void export() throws IOException{
		
		String content = "\t";
		 
		File file = new File(project+"CoChanges.txt");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		
		
		PPLTable assistantTable = allTables.firstEntry().getValue();

		for ( String key : assistantTable.getCoChanges().keySet() ){
			
			content=content+key+"\t";
			
		}
		
		content=content+"\n";
		
		
		for ( String key : assistantTable.getCoChanges().keySet() ){
			
			content=content+key;
			
			for (Map.Entry<String,PPLTable> tb : allTables.entrySet()) {
				
				if(tb.getKey().equals(key)){
					
					for ( String key1 : tb.getValue().getCoChanges().keySet() ) {
						
						if(!key1.equals(key)){
						
							content=content+"\t";
							content=content+tb.getValue().getCoChanges().get(key1);
						
						}
						else{
							
							content=content+"\t";
							content=content+"-";
										
						}
						
					}
					
					content=content+"\n";
					
				}
				
			}
			
		}
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();		
		
		System.out.println("CoChanges Report ready");

	}
	
	private void exportSequenceCoChanges() throws IOException{
		
		String content = "\t";
		 
		File file = new File(project+"SequenceCoChanges.txt");

		if (!file.exists()) {
			file.createNewFile();
		}

		PPLTable assistantTable = allTables.firstEntry().getValue();
		
		for ( String key : assistantTable.getSequenceCoChanges().keySet() ){
			
			content=content+key+"\t";
			
		}
		
		content=content+"\n";
		
		
		for ( String key : assistantTable.getSequenceCoChanges().keySet() ){
			
			content=content+key;
			
			for (Map.Entry<String,PPLTable> tb : allTables.entrySet()) {
				
				if(tb.getKey().equals(key)){
					
					for ( String key1 : tb.getValue().getSequenceCoChanges().keySet() ) {
						
						if(!key1.equals(key)){

							content=content+"\t";
							content=content+tb.getValue().getSequenceCoChanges().get(key1);
							
						}
						else{
							
							content=content+"\t";
							content=content+"-";
							
						}
		
					}
					
					content=content+"\n";
					
				}
				
			}
			
		}
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();		
		
		System.out.println("SequenceCoChanges Report ready");

	}
	
	private void exportWindowCoChanges() throws IOException{
		
		String content = "\t";
		 
		File file = new File(project+"WindowCoChanges.txt");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		PPLTable assistantTable = allTables.firstEntry().getValue();
		
		for ( String key : assistantTable.getWindowCoChanges().keySet() ){
			
			content=content+key+"\t";
			
		}
		
		content=content+"\n";
		
		for ( String key : assistantTable.getWindowCoChanges().keySet() ){
			
			content=content+key;
			
			for (Map.Entry<String,PPLTable> tb : allTables.entrySet()) {
				
				if(tb.getKey().equals(key)){
					
					for ( String key1 : tb.getValue().getWindowCoChanges().keySet() ) {
						
						if(!key1.equals(key)){

							content=content+"\t";							
							content=content+tb.getValue().getWindowCoChanges().get(key1);
							
						}
						else{
							
							content=content+"\t";
							content=content+"-";
							
						}

					}
					
					content=content+"\n";
					
				}
				
			}
			
		}
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();	
		
		System.out.println("WindowCoChanges Report ready");
		
	}


	@Override
	public Results compute() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void compute(String compute) {

		computeCoChanges();
		computeSequenceCoChanges();
		computeWindowCoChanges();
		try {
			export();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			exportSequenceCoChanges();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			exportWindowCoChanges();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
}

