package tableClustering.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import tableClustering.commons.Cluster;
import tableClustering.commons.ClusterCollector;
import data.dataKeeper.GlobalDataKeeper;
import data.pplSqlSchema.PPLTable;


public class AgglomerativeClusterExtractor implements ClusterExtractor{

	

	@Override
	public ClusterCollector extractAtMostKClusters(GlobalDataKeeper dataKeeper,
			int numClusters, float birthWeight, float deathWeight, float changeWeight) {
		
		ClusterCollector initSolution = new ClusterCollector();
		this.init(dataKeeper, initSolution);
		//System.out.println("init "+initSolution.getPhases().size());
		//this.preProcessingTime(transitionHistory, initSolution);
		//System.out.println("timePreProcessing "+initSolution.getPhases().size());

		
		
		ClusterCollector currentSolution = new ClusterCollector();
		currentSolution = this.newClusterCollector(initSolution, birthWeight, deathWeight, changeWeight,dataKeeper.getAllPPLSchemas().size()-1);

		while (currentSolution.getClusters().size() > numClusters){

			currentSolution = this.newClusterCollector(currentSolution, birthWeight, deathWeight, changeWeight,dataKeeper.getAllPPLSchemas().size()-1);
		}
		return currentSolution;
		
	}
	
	public ClusterCollector newClusterCollector(ClusterCollector prevCollector,float birthWeight, float deathWeight ,float changeWeight,int dbDuration){
		
		ClusterCollector newCollector = new ClusterCollector();
		ArrayList<Cluster> newClusters = new ArrayList<Cluster>();
		ArrayList<Cluster> oldClusters = prevCollector.getClusters();
		int oldSize = oldClusters.size();
		if (oldSize == 0){
			
			System.out.println("Sth went terribly worng at method XXX");
			System.exit(-10);
		}

		//compute the distances for all the bloody phases
		//TODO add it at phase collector to move on !$#@$#%$^$%&%&
		double distances[] = new double[oldSize];
		distances[0] = Double.MAX_VALUE;
		int pI = 0;
		
	    Iterator<Cluster> clusterIter = oldClusters.iterator();
	    Cluster previousCluster = clusterIter.next();
	    while (clusterIter.hasNext()){
	      Cluster c = clusterIter.next();
	      pI++;
	      distances[pI] = c.distance(previousCluster,birthWeight,deathWeight,changeWeight,dbDuration);
	      
	      previousCluster = c;
	    }


		//find the two most similar phases in the old collection
	    int posI=-1; double minDist = Double.MAX_VALUE;
	    for(int i=1; i<oldSize; i++){
	    	if(distances[i]<minDist){
	    		posI = i;
	    		minDist = distances[i];
	    	}
	    }
	    //merge them in a new phase. Merge posI with its PREVIOUS (ATTN!!)
		Cluster toMerge = oldClusters.get(posI-1);
		Cluster newCluster = toMerge.mergeWithNextCluster(oldClusters.get(posI));
		//System.out.println("HUE:"+newCluster.getBirth()+"\t"+newCluster.getDeath());
		for(int i=0; i < posI-1; i++){
			Cluster c = oldClusters.get(i);
			newClusters.add(c);
		}
		//add the new i_new = merge(i,i+1) to the new phases
		newClusters.add(newCluster);
		//add the i+1, .. last to the new phases
		if(posI<oldSize-1){
			for(int i=posI+1; i < oldSize; i++){
				Cluster c = oldClusters.get(i);
				newClusters.add(c);
			}		
		}
		newCollector.setClusters(newClusters);

		return newCollector;
	}
	
	
	public ClusterCollector init(GlobalDataKeeper dataKeeper, ClusterCollector clusterCollector){
		
		TreeMap<String, PPLTable> tables=dataKeeper.getAllPPLTables();

		
		for (Map.Entry<String,PPLTable> pplTable : tables.entrySet()) {
			Cluster c = new Cluster(pplTable.getValue().getBirthVersionID(),pplTable.getValue().getDeath(),pplTable.getValue().getDeathVersionID(),pplTable.getValue().getDeath(),pplTable.getValue().getTotalChanges());
			c.addTable(pplTable.getValue());
			clusterCollector.addCluster(c);
			
		}
		
		System.out.println("HUE:"+clusterCollector.getClusters().size());
		return clusterCollector;
	}

}
