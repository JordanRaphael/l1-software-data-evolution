package data.dataKeeper;

import java.util.ArrayList;

import phaseAnalyzer.commons.PhaseCollector;
import tableClustering.clusterExtractor.commons.ClusterCollector;

public class DataCollectorsManager {
	
	private ArrayList<PhaseCollector> phaseCollectors = null;
	private ArrayList<ClusterCollector> clusterCollectors = null;
	
	protected DataCollectorsManager() {
		phaseCollectors = new ArrayList<PhaseCollector>();
		clusterCollectors = new ArrayList<ClusterCollector>();
	}
	
	protected void setPhaseCollectors(ArrayList<PhaseCollector>  phaseCollectors){
		this.phaseCollectors = phaseCollectors;
	}
	
	protected ArrayList<PhaseCollector> getPhaseCollectors(){
		return phaseCollectors;
	}
	
	protected void setClusterCollectors(ArrayList<ClusterCollector> clusterCollectors){
		this.clusterCollectors = clusterCollectors;
	}
	
	protected ArrayList<ClusterCollector> getClusterCollectors(){
		return clusterCollectors;
	}

}
