package data.sorters;

import java.util.Map;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import data.dataKeeper.GlobalDataKeeper;
import data.pplSqlSchema.PPLTable;

public class PldRowSorter {
	
	private String[][] finalRows;
	private GlobalDataKeeper globalDataKeeper = new GlobalDataKeeper();

	public PldRowSorter(String[][] finalRows,GlobalDataKeeper globalDataKeeper){
		this.finalRows=finalRows;
		this.globalDataKeeper=globalDataKeeper;
	}
	
	public String[][] sortRows(){
		
		String[][] sortedRows=new String[finalRows.length][finalRows[0].length];
		
		PPLTableSortingClass tablesSorter=new PPLTableSortingClass();
		
	    //System.out.println(lala.entriesSortedByValues(globalDataKeeper.getAllPPLTables());
	    Map<String, PPLTable> wtf=globalDataKeeper.getAllPPLTables();
	    System.out.println(tablesSorter.entriesSortedByValues(wtf).size());
	    int counter=0;
	    for(Map.Entry<String, PPLTable> ppl:tablesSorter.entriesSortedByValues(wtf)){
			System.out.println(ppl.getKey());
			for(int i=0; i<finalRows.length; i++ ){
				if (finalRows[i][0].equals(ppl.getKey())) {
					for(int j=0;j<finalRows[0].length; j++){
						sortedRows[counter][j]=finalRows[i][j];
					}                               
				}
			}
			
			counter++;
		}
		System.out.println(sortedRows.length+" "+sortedRows[0].length);
		
		for(int i=0; i<finalRows.length; i++ ){
				System.out.println(i);
				for(int j=0;j<finalRows[0].length; j++){
					System.out.print(finalRows[i][j]+"\t");
				}
				System.out.println("\n");
		}
		
		return sortedRows;
		
		
	}
	
}
