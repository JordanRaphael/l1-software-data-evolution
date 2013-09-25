package gui;

import java.util.ArrayList;

import sqlSchema.Schema;
import sqlSchema.Table;
import transitions.Deletion;
import transitions.Insersion;
import transitions.Transition;
import transitions.TransitionList;
import transitions.Update;
import java.lang.Math;

public class TableConstruction {
	
	
	private static ArrayList<Schema> AllSchemas=new ArrayList<Schema>();
	private ArrayList<TransitionList> AllTransitions=new ArrayList<TransitionList>();
	private int columnsNumber=0;
	private Integer[][] schemaColumnId=null;
	private int maxDeletions=1;
	private int maxInsersions=1;
	private int maxUpdates=1;
	private Integer segmentSize[]=new Integer[3];
	private int totalChanges=0;
	
	public TableConstruction(ArrayList<Schema> tmpAllSchemas,
			ArrayList<TransitionList> tmpAllTransitions){
		
		AllSchemas=tmpAllSchemas;
		AllTransitions=tmpAllTransitions;
		
		
	}
	
	public String[] constructColumns(){
		
		ArrayList<String> columnsList=new ArrayList<String>();
		
		schemaColumnId=new Integer[AllSchemas.size()][2];
		
		for(int i=0;i<AllSchemas.size();i++){
			schemaColumnId[i][0]=i;
			if(i==0){
				schemaColumnId[i][1]=1;
			}
			else{
				schemaColumnId[i][1]=schemaColumnId[i-1][1]+4;
			}
		}
		
		columnsList.add("Table name");
		
		for(int i=0; i<AllSchemas.size(); i++){
			
			if(i<AllSchemas.size()-1){
				columnsList.add(AllSchemas.get(i).getName().replaceAll(".sql", ""));
			
				for(int j=0; j<3; j++){
					
					switch(j){
						
						case 0: columnsList.add("I");
								break;
							
						case 1: columnsList.add("U");
								break;
								
						case 2: columnsList.add("D");
								break;
							
						default:break;
					}
					
					
				}
			
			}
			else{
				
				columnsList.add(AllSchemas.get(i).getName().replaceAll(".sql", ""));
			}
				
		}
		
		columnsNumber=columnsList.size();
		String[] tmpcolumns=new String[columnsList.size()];
		
		for(int i=0; i<columnsList.size(); i++ ){
			
			tmpcolumns[i]=columnsList.get(i);
			
		}
		
		return(tmpcolumns);
		
		
	}
	
	public String[][] constructRows(){
		
		ArrayList<String[]> allRows=new ArrayList<String[]>();
		ArrayList<String>	allTables=new ArrayList<String>();
		int found=0;
		
		for(int i=0; i<AllSchemas.size(); i++){
			
			Schema oneSchema=AllSchemas.get(i);
			
			
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
					
					allTables.add(tmpTableName);
					String[] tmpOneRow=constructOneRow(oneTable,i);
					allRows.add(tmpOneRow);
					AllSchemas.get(i).getTableAt(j).setTotalChanges(totalChanges);
					totalChanges=0;
					oneTable=new Table();
					tmpOneRow=new String[columnsNumber];
				}
				else{
					found=0;
				}
				
				
			}
		}
		
		String[][] tmpRows=new String[allRows.size()][columnsNumber];
		
		for(int i=0; i<allRows.size(); i++){
			
			String[] tmpOneRow=allRows.get(i);
			for(int j=0; j<tmpOneRow.length; j++ ){
				
				tmpRows[i][j]=tmpOneRow[j];
				
				
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
	
	private String[] constructOneRow(Table oneTable,int schemaVersion){
		
		String[] oneRow=new String[columnsNumber];
		int deletedAllTable=0;
		int pointerCell=0;
		int updn=0;
		int deln=0;
		int insn=0;
		oneRow[pointerCell]=oneTable.getName();
		
		if(schemaVersion==0){
			pointerCell++;
			oneRow[pointerCell]="---------";
			pointerCell++;
			
		}
		else{
			
			for(int i=0; i<schemaColumnId.length; i++){
				
				if(schemaVersion==schemaColumnId[i][0]){
					pointerCell=schemaColumnId[i][1]-3;
					break;
				}
				
			}
			
		}
		
		
		int initialization=0;
		if(schemaVersion>0){
			initialization=schemaVersion-1;
		}
		for(int i=initialization; i<AllTransitions.size(); i++){
			
			TransitionList  tmpTL=AllTransitions.get(i);
			String sc=tmpTL.getNewVersion();
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
							 
							 if(deln>maxDeletions){
									maxDeletions=deln;
									
							 }
							 
							 
							 totalChanges=totalChanges+deln;
							 
							 int num=getNumOfAttributesOfNextSchema(sc, oneTable.getName());
							 
							 if(num==0){
								 
								 deletedAllTable=1;
							 }
							 
						 }
						 
					 }
					 
					 if(tmpTR.get(j) instanceof Insersion){
						
						 Insersion ins=(Insersion) tmpTR.get(j);
						 
						 if(ins.getAffTable().getName().equals(oneTable.getName())){
								
							insn=ins.getNumOfAffAttributes(); 
							
							if(insn>maxInsersions){
								maxInsersions=insn;
							}
						
							totalChanges=totalChanges+insn;
							//****************
							//deletedAllTable=0;
						 }
						 
						 
					 }
					 
					 if(tmpTR.get(j) instanceof Update){
							
						 Update up=(Update) tmpTR.get(j);
						 
						 if(up.getAffTable().getName().equals(oneTable.getName())){
								
							updn=up.getNumOfAffAttributes();
							
							if(updn>maxUpdates){
								maxUpdates=updn;
							}
							
							totalChanges=totalChanges+updn;
						 }
						 
						 
					 }
					 
					 
				}
			}
			if(pointerCell>=columnsNumber){
				
				break;
			}
			
			
			oneRow[pointerCell]=Integer.toString(insn);
			pointerCell++;
			oneRow[pointerCell]=Integer.toString(updn);
			pointerCell++;
			oneRow[pointerCell]=Integer.toString(deln);
			pointerCell++;
			if(deletedAllTable==1){
				break;
			}
			oneRow[pointerCell]="------";
			pointerCell++;
			
			insn=0;
			updn=0;
			deln=0;
			
			
		}
		
		for(int i=0; i<oneRow.length; i++){
			if(oneRow[i]==null){
				oneRow[i]="";
			}
		}
	
		return oneRow;
		
	}
	
	public Integer[] getSegmentSize(){
		return segmentSize;
	}
	
	private int getNumOfAttributesOfNextSchema(String schema,String table){
		int num = 0;
		Schema sc=null;
	
		for(int i=0; i<AllSchemas.size(); i++){
			if(AllSchemas.get(i).getName().equals(schema)){
				sc=AllSchemas.get(i);
				break;
			}
		}
		for(int i=0;i<sc.getTables().size();i++){
			if(sc.getTableAt(i).getName().equals(table)){
				num=sc.getTableAt(i).getAttrs().size();
				return num;
			}
		}
		return num;
	}

}
