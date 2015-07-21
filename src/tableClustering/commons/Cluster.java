package tableClustering.commons;

import java.util.Map;
import java.util.TreeMap;

import data.pplSqlSchema.PPLTable;

public class Cluster {

	private int birth;
	private int death;
	private int totalChanges=0;
	private TreeMap<String,PPLTable> tables=null;
	
	public Cluster(){
		
		tables=new TreeMap<String, PPLTable>();
	}
	
	public Cluster(int birth, int death, int totalChanges){
		
		this.birth=birth;
		this.death=death;
		this.totalChanges=totalChanges;
		tables=new TreeMap<String, PPLTable>();

		
	}
	
	public TreeMap<String,PPLTable> getTables(){
		return tables;
	}
	
	public double distance(Cluster anotherCluster,float birthWeight, float deathWeight ,float changeWeight){
		
		double changeDistance = Math.abs(this.totalChanges - anotherCluster.totalChanges);
	
		double birthDistance = Math.abs(this.birth-anotherCluster.birth);
		double deathDistance = Math.abs(this.death-anotherCluster.death);


		double totalDistance = changeWeight * changeDistance + birthWeight * birthDistance + deathWeight * deathDistance;
		return totalDistance;
		
	}
	
	public Cluster mergeWithNextCluster(Cluster nextCluster){
		
		Cluster newCluster = new Cluster();
			
		newCluster.birth = this.birth;
		newCluster.death = nextCluster.death;
		
		newCluster.totalChanges = this.totalChanges + nextCluster.totalChanges;
		
		for (Map.Entry<String,PPLTable> tab : tables.entrySet()) {
			
			newCluster.getTables().put(tab.getKey(), tab.getValue());
			
		}
		
		for (Map.Entry<String,PPLTable> tabNext : nextCluster.getTables().entrySet()) {
			
			newCluster.getTables().put(tabNext.getKey(), tabNext.getValue());
			
		}
		
		//System.out.println(subPhases.size());
		//TODO FIX FIX FIX FIX
		//Add any other attributes necessary!!
		return newCluster;
	}
	
	
}
