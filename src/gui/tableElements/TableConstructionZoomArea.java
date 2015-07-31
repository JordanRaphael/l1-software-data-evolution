package gui.tableElements;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.sun.org.apache.bcel.internal.classfile.PMGClass;

import data.dataKeeper.GlobalDataKeeper;
import data.pplSqlSchema.PPLSchema;
import data.pplSqlSchema.PPLTable;
import data.pplTransition.AtomicChange;
import data.pplTransition.PPLTransition;
import data.pplTransition.TableChange;

public class TableConstructionZoomArea implements Pld {

	private static TreeMap<String,PPLSchema> allPPLSchemas=new TreeMap<String,PPLSchema>();
	private static TreeMap<String,PPLSchema> selectedPPLSchemas=new TreeMap<String,PPLSchema>();
	private ArrayList<PPLTable>	tables=new ArrayList<PPLTable>();
	private TreeMap<String,PPLTable> selectedTables = new TreeMap<String,PPLTable>();
	private ArrayList<String> sSelectedTables = new ArrayList<String>();
	private TreeMap<Integer,PPLTransition> pplTransitions = new TreeMap<Integer,PPLTransition>();
	private GlobalDataKeeper globalDataKeeper = new GlobalDataKeeper();
	private int selectedColumn;

	private int columnsNumber=0;
	private Integer[][] schemaColumnId=null;
	private int maxDeletions=1;
	private int maxInsersions=1;
	private int maxUpdates=1;
	private Integer segmentSize[]=new Integer[3];
	
	public TableConstructionZoomArea(GlobalDataKeeper globalDataKeeper,ArrayList<String> sSelectedTables,int selectedColumn){
		
		this.globalDataKeeper=globalDataKeeper;
		allPPLSchemas=globalDataKeeper.getAllPPLSchemas();
		this.sSelectedTables=sSelectedTables;
		this.selectedColumn=selectedColumn;
		fillSelectedPPLTransitions();
		fillSelectedPPLSchemas();
		fillSelectedTables();
	}
	
	private void fillSelectedPPLTransitions() {
		
		if(selectedColumn==0){
			pplTransitions=globalDataKeeper.getAllPPLTransitions();
		}
		else{
			pplTransitions=globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(selectedColumn-1).getPhasePPLTransitions();

		}
		/*
		for (Map.Entry<Integer,PPLTransition> pplTr : pplTransitions.entrySet()) {
			System.out.println(pplTr.getKey());
		}
		*/
	}
	
	private void fillSelectedPPLSchemas(){
		
		for (Map.Entry<Integer,PPLTransition> pplTr : pplTransitions.entrySet()) {
			
			selectedPPLSchemas.put(pplTr.getValue().getOldVersionName(), allPPLSchemas.get(pplTr.getValue().getOldVersionName()));
			
		}
		/*for (Map.Entry<String,PPLSchema> pplSc : selectedPPLSchemas.entrySet()) {
			System.out.println(pplSc.getKey());
		}*/
		
		
	}
	
	private void fillSelectedTables(){
		
		for(int i=0; i<sSelectedTables.size(); i++){
			selectedTables.put(sSelectedTables.get(i),this.globalDataKeeper.getAllPPLTables().get(sSelectedTables.get(i)) );
		}
		
		/*for (Map.Entry<String,PPLTable> pplTab : selectedTables.entrySet()) {
			System.out.println(pplTab.getKey());
		}*/
		
	}
	
	public String[] constructColumns(){
		
		ArrayList<String> columnsList=new ArrayList<String>();
		
		schemaColumnId=new Integer[pplTransitions.size()][2];
		
		for(int i=0;i<pplTransitions.size();i++){
			schemaColumnId[i][0]=i;
			if(i==0){
				schemaColumnId[i][1]=1;
			}
			else{
				schemaColumnId[i][1]=schemaColumnId[i-1][1]+1;
			}
		}
		
		
		columnsList.add("Table name");
		
		
		for (Map.Entry<Integer,PPLTransition> pplTr : pplTransitions.entrySet()) {
			
				String label=Integer.toString(pplTr.getKey());
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
		//int i=0;
		
		//for (Map.Entry<String,PPLSchema> pplSc : selectedPPLSchemas.entrySet()) {
			
			//PPLSchema oneSchema=pplSc.getValue();
			
			
			//for(int j=0; j<oneSchema.getTables().size(); j++){
			for(Map.Entry<String, PPLTable> oneTable:selectedTables.entrySet()){
				
					
				
					//PPLTable oneTable=oneSchema.getTableAt(j);
					
					String tmpTableName=oneTable.getKey();
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
						tables.add(oneTable.getValue());
						String[] tmpOneRow=constructOneRow(oneTable.getValue());
						allRows.add(tmpOneRow);
						//oneTable=new PPLTable();
						tmpOneRow=new String[columnsNumber];
					}
					else{
						found=0;
					}
				
				
			}
			
			//i++;
		//}
		
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
		
		return tmpRows;
		
	}
	
	private String[] constructOneRow(PPLTable oneTable){
		
		String[] oneRow=new String[columnsNumber];
		int deletedAllTable=0;
		int pointerCell=0;
		int updn=0;
		int deln=0;
		int insn=0;
		int totalChangesForOneTransition=0;
		oneRow[pointerCell]=oneTable.getName();
		boolean exists=false;
		for (Map.Entry<Integer,PPLTransition> pplTr : pplTransitions.entrySet()) {
						
			pointerCell++;
			
			PPLSchema oldS = allPPLSchemas.get(pplTr.getValue().getOldVersionName());
			PPLSchema newS = allPPLSchemas.get(pplTr.getValue().getNewVersionName());

			if(oldS.getTables().containsKey(oneTable.getName())|| newS.getTables().containsKey(oneTable.getName())){
				exists=true;
				break;
			}
			
		}
		
		System.out.println(oneTable.getName()+" "+pointerCell+" "+selectedPPLSchemas.size()+" "+pplTransitions.size());
		
		
		
		int initialization=pointerCell-1;
		
		
		Integer[] mapKeys = new Integer[pplTransitions.size()];
		int pos2 = 0;
		for (Integer key : pplTransitions.keySet()) {
		    mapKeys[pos2++] = key;
		}
		
		Integer pos3=null;
		if(exists){
			for(int i=initialization; i<pplTransitions.size(); i++){
				
				pos3=mapKeys[i];
				
				PPLTransition  tmpTL=pplTransitions.get(pos3);
				
				String sc=tmpTL.getNewVersionName();
				
				ArrayList<TableChange> tmpTR=tmpTL.getTableChanges();
				
				updn=0;
				deln=0;
				insn=0;
				
				if(tmpTR!=null){
					totalChangesForOneTransition=0;
					
					for(int j=0; j<tmpTR.size(); j++){
						
						TableChange tableChange=tmpTR.get(j);
						
						if(tableChange.getAffectedTableName().equals(oneTable.getName())){
							
							
							
							ArrayList<AtomicChange> atChs = tableChange.getTableAtChForOneTransition();
							
							for(int k=0; k<atChs.size(); k++){
								
								
								
								if (atChs.get(k).getType().contains("Addition")){
									
									insn++;
									
									if(insn>maxInsersions){
										maxInsersions=insn;
									}
									//totalChangesForOneTransition=totalChangesForOneTransition+insn;
									
								}
								else if(atChs.get(k).getType().contains("Deletion")){
									
									deln++;
									
									 if(deln>maxDeletions){
											maxDeletions=deln;
											
									 }
									 
									//totalChangesForOneTransition=totalChangesForOneTransition+deln;
									 
									 int num=getNumOfAttributesOfNextSchema(sc, oneTable.getName());
									 
									 if(num==0){
										 
										 deletedAllTable=1;
									 }
								}
								else{
									
									updn++;
									
									if(updn>maxUpdates){
										maxUpdates=updn;
									}
									
									//totalChangesForOneTransition=totalChangesForOneTransition+updn;
									
								}
								
							}
						}
						 
						 
					}
					
					
				}
				System.out.println("poc:"+pointerCell+" "+columnsNumber );
				if(pointerCell>=columnsNumber){
					
					break;
				}
				
				totalChangesForOneTransition=insn+deln+updn;
				oneRow[pointerCell]=Integer.toString(totalChangesForOneTransition);
				/*pointerCell++;
				oneRow[pointerCell]=Integer.toString(updn);
				pointerCell++;
				oneRow[pointerCell]=Integer.toString(deln);*/
				pointerCell++;
				if(deletedAllTable==1){
					break;
				}
				//oneRow[pointerCell]="------";
				//pointerCell++;
				
				insn=0;
				updn=0;
				deln=0;
				
				
			}
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
	
	private int getNumOfAttributesOfNextSchema(String schema,String table){
		int num = 0;
		PPLSchema sc=allPPLSchemas.get(schema);
		
		/*for(int i=0; i<allPPLSchemas.size(); i++){
			if(allPPLSchemas.get(i).getName().equals(schema)){
				sc=allPPLSchemas.get(schema);
				break;
			}
		}*/
		
		
		for(int i=0;i<sc.getTables().size();i++){
			if(sc.getTableAt(i).getName().equals(table)){
				num=sc.getTableAt(i).getAttrs().size();
				return num;
			}
		}
		return num;
	}


}
