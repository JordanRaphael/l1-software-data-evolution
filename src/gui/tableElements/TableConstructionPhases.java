package gui.tableElements;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import phaseAnalyzer.commons.Phase;
import data.dataKeeper.GlobalDataKeeper;
import data.pplSqlSchema.PPLSchema;
import data.pplSqlSchema.PPLTable;
import data.pplTransition.AtomicChange;
import data.pplTransition.PPLTransition;
import data.pplTransition.TableChange;

public class TableConstructionPhases implements Pld {

	
	private static TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private ArrayList<PPLTable>	tables=new ArrayList<PPLTable>();
	//private TreeMap<Integer,PPLTransition> allPPLTransitions = new TreeMap<Integer,PPLTransition>();
	private ArrayList<Phase> phases = new ArrayList<Phase>();


	private int columnsNumber=0;
	private Integer[][] schemaColumnId=null;
	private int maxDeletions=1;
	private int maxInsersions=1;
	private int maxUpdates=1;
	private int maxTotalChangesForOneTr=1;
	private Integer[] segmentSize=new Integer[4];
	private int totalChanges=0;
	
	public TableConstructionPhases(GlobalDataKeeper globalDataKeeper){
		
		allPPLSchemas=globalDataKeeper.getAllPPLSchemas();
		phases=globalDataKeeper.getPhaseCollectors().get(0).getPhases();
		//allPPLTransitions=globalDataKeeper.getAllPPLTransitions();
		
		
	}
	
public String[] constructColumns(){
		
		ArrayList<String> columnsList=new ArrayList<String>();
		
		schemaColumnId=new Integer[phases.size()][2];
		
		for(int i=0;i<phases.size();i++){
			schemaColumnId[i][0]=i;
			if(i==0){
				schemaColumnId[i][1]=1;
			}
			else{
				schemaColumnId[i][1]=schemaColumnId[i-1][1]+1;
			}
		}
		
		
		
		columnsList.add("Table name");
		
		
		for(int i=0;i<phases.size(); i++){
			String label="Phase "+i;
			columnsList.add(label);
		}
		
		columnsNumber=columnsList.size();
		String[] tmpcolumns=new String[columnsList.size()];
		
		for(int j=0; j<columnsList.size(); j++ ){
			
			tmpcolumns[j]=columnsList.get(j);
			
		}
		
		return(tmpcolumns);
		
		
	}
	
	public String[][] constructRows(){
		
		ArrayList<String[]> allRows=new ArrayList<String[]>();
	    ArrayList<String>	allTables=new ArrayList<String>();

		
		int found=0;
		int i=0;
		
		
		
		
		for (Map.Entry<String,PPLSchema> pplSc : allPPLSchemas.entrySet()) {
			
			PPLSchema oneSchema=pplSc.getValue();
			
			
			for(int j=0; j<oneSchema.getTables().size(); j++){
				
				PPLTable oneTable=oneSchema.getTableAt(j);
				
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
					
					allTables.add(tmpTableName);
					tables.add(oneTable);
					String[] tmpOneRow=constructOneRow(oneTable,i,oneSchema.getName());
					allRows.add(tmpOneRow);
					allPPLSchemas.get(pplSc.getKey()).getTableAt(j).setTotalChanges(totalChanges);
					totalChanges=0;
					oneTable=new PPLTable();
					tmpOneRow=new String[columnsNumber];
				}
				else{
					found=0;
				}
				
				
			}
			
			i++;
		}
		
		String[][] tmpRows=new String[allRows.size()][columnsNumber];
		
		for(int z=0; z<allRows.size(); z++){
			
			String[] tmpOneRow=allRows.get(z);
			for(int j=0; j<tmpOneRow.length; j++ ){
				
				tmpRows[z][j]=tmpOneRow[j];
				
				
			}
			
		}
		
		float maxI=(float) maxInsersions/4;
		segmentSize[0]=(int) Math.rint(maxI);
		
		float maxU=(float) maxUpdates/4;
		segmentSize[1]=(int) Math.rint(maxU);
		
		float maxD=(float) maxDeletions/4;
		segmentSize[2]=(int) Math.rint(maxD);
		
		float maxT=(float) maxTotalChangesForOneTr/4;
		segmentSize[3]=(int) Math.rint(maxT);
		
		return tmpRows;
		
	}
	
	private String[] constructOneRow(PPLTable oneTable,int schemaVersion,String schemaName){
		
		String[] oneRow=new String[columnsNumber];
		int deletedAllTable=0;
		int pointerCell=0;
		int updn=0;
		int deln=0;
		int insn=0;
		int totalChangesForOnePhase=0;
		oneRow[pointerCell]=oneTable.getName();
		if(schemaVersion==0){
			pointerCell++;
			
			
		}
		else{
			for(int p=0; p<phases.size(); p++){
				
				TreeMap<Integer,PPLTransition> phasePPLTransitions=phases.get(p).getPhasePPLTransitions();
				for(Map.Entry<Integer, PPLTransition> tr:phasePPLTransitions.entrySet()){
					//System.out.println(oneTable.getName()+" "+schemaName+" "+tr.getValue().getOldVersionName()+" "+tr.getValue().getNewVersionName());
					if(tr.getValue().getOldVersionName().equals(schemaName)|| tr.getValue().getNewVersionName().equals(schemaName)){
						pointerCell=p;
						break;
					}
				}
				
			}
			
			
//			for(int i=0; i<schemaColumnId.length; i++){
//				
//				if(schemaVersion==schemaColumnId[i][0]){
//					pointerCell=schemaColumnId[i][1];
//					break;
//				}
//				
//			}
			
		}
		System.out.println(oneTable.getName()+" "+pointerCell);
		
		int initialization=0;
		if(schemaVersion>0){
			initialization=schemaVersion-1;
		}
		
		for(int p=0; p<phases.size(); p++){
			
			TreeMap<Integer,PPLTransition> phasePPLTransitions=phases.get(p).getPhasePPLTransitions();
			if (totalChangesForOnePhase>maxTotalChangesForOneTr) {
				maxTotalChangesForOneTr=totalChangesForOnePhase;
			}
			totalChangesForOnePhase=0;
			/*
			Integer[] mapKeys = new Integer[phasePPLTransitions.size()];
			int pos2 = 0;
			for (Integer key : phasePPLTransitions.keySet()) {
			    mapKeys[pos2++] = key;
			}
			
			Integer pos3=null;
			*/
			//for(int i=0; i<phasePPLTransitions.size(); i++){
			
			for(Map.Entry<Integer, PPLTransition> tmpTL:phasePPLTransitions.entrySet()){

				//pos3=mapKeys[i];
				
				//PPLTransition  tmpTL=phasePPLTransitions.get(pos3);
				String sc=tmpTL.getValue().getNewVersionName();
				
				ArrayList<TableChange> tmpTR=tmpTL.getValue().getTableChanges();
				
				updn=0;
				deln=0;
				insn=0;
				
				if(tmpTR!=null){
					
					for(int j=0; j<tmpTR.size(); j++){
						
						TableChange tableChange=tmpTR.get(j);
						//System.out.println(tableChange.getAffectedTableName()+":"+oneTable.getName()+"!");
						//System.out.println(tableChange.getAffectedTableName()+":");
						if(tableChange.getAffectedTableName().equals(oneTable.getName())){
							
							
							ArrayList<AtomicChange> atChs = tableChange.getTableAtChForOneTransition();
							for(int k=0; k<atChs.size(); k++){
								
								if (atChs.get(k).getType().contains("Addition")){
									
									insn++;
									
									if(insn>maxInsersions){
										maxInsersions=insn;
									}
									totalChangesForOnePhase=totalChangesForOnePhase+insn;
									totalChanges=totalChanges+insn;
									
								}
								else if(atChs.get(k).getType().contains("Deletion")){
									
									deln++;
									
									 if(deln>maxDeletions){
											maxDeletions=deln;
											
									 }
									 
									totalChangesForOnePhase=totalChangesForOnePhase+deln;
									 totalChanges=totalChanges+deln;
									 
									 boolean existsLater=getNumOfAttributesOfNextSchema(sc, oneTable.getName());
									 
									 if(!existsLater){
										 
										 deletedAllTable=1;
									 }
								}
								else{
									
									updn++;
									
									if(updn>maxUpdates){
										maxUpdates=updn;
									}
									
									totalChangesForOnePhase=totalChangesForOnePhase+updn;
									totalChanges=totalChanges+updn;
									
								}
								
							}
						}
						 
						 
					}
					
					
				}
				
				if(pointerCell>=columnsNumber){
					
					break;
				}
				
				
				oneRow[pointerCell]=Integer.toString(totalChangesForOnePhase);
				/*pointerCell++;
				oneRow[pointerCell]=Integer.toString(updn);
				pointerCell++;
				oneRow[pointerCell]=Integer.toString(deln);*/
				if(deletedAllTable==1){
					break;
				}
				//oneRow[pointerCell]="------";
				//pointerCell++;
				
				insn=0;
				updn=0;
				deln=0;
				
				
			}
			if(deletedAllTable==1){
				break;
			}
			pointerCell++;

		}
		
		for(int i=0; i<oneRow.length; i++){
			if(oneRow[i]==null){
				oneRow[i]="";
			}
		}

		String lala="";
		for (int i = 0; i < oneRow.length; i++) {
			lala=lala+oneRow[i]+",";
		}
		System.out.println(oneTable.getName()+" "+lala);
		return oneRow;

		
		
	}
	
	public Integer[] getSegmentSize(){
		return segmentSize;
	}
	
	private boolean getNumOfAttributesOfNextSchema(String schema,String table){
		PPLSchema sc=allPPLSchemas.get(schema);
		
		/*for(int i=0; i<allPPLSchemas.size(); i++){
			if(allPPLSchemas.get(i).getName().equals(schema)){
				sc=allPPLSchemas.get(schema);
				break;
			}
		}*/
		
		if(sc.getTables().containsKey(table)){
			return true;
		}
		else {
			return false;
		}
		/*
		for(int i=0;i<sc.getTables().size();i++){
			if(sc.getTableAt(i).getName().equals(table)){
				num=sc.getTableAt(i).getAttrs().size();
				return num;
			}
		}
		return num;*/
	}

}
