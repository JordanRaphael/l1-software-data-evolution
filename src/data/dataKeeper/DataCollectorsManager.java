package data.dataKeeper;

import java.util.ArrayList;

import data.tableConstructors.TableConstructionWithClusters;
import phaseAnalyzer.commons.PhaseCollector;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.commons.ClusterCollector;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class DataCollectorsManager {
	
	private ArrayList<PhaseCollector> phaseCollectors = null;
	private ArrayList<ClusterCollector> clusterCollectors = null;
	
	public DataCollectorsManager() {
		
		phaseCollectors = new ArrayList<PhaseCollector>();
		clusterCollectors = new ArrayList<ClusterCollector>();
	}
	
	public void setPhaseCollectors(ArrayList<PhaseCollector>  phaseCollectors){
		this.phaseCollectors = phaseCollectors;
	}
	
	protected ArrayList<PhaseCollector> getPhaseCollectors(){
		return phaseCollectors;
	}
	
	public void setClusterCollectors(ArrayList<ClusterCollector> clusterCollectors){
		this.clusterCollectors = clusterCollectors;
	}
	
	protected ArrayList<ClusterCollector> getClusterCollectors(){
		return clusterCollectors;
	}
	
	public void populateWithPhases(PhaseAnalyzerMainEngine mainEngine, Integer numberOfPhases, GlobalDataManager globalDataManager) {
		
		mainEngine.parseInput();
		System.out.println("\n\n\n");
		mainEngine.extractPhases(numberOfPhases);
		mainEngine.connectTransitionsWithPhases(globalDataManager);
		setPhaseCollectors(mainEngine.getPhaseCollectors());
	}
	
	public void populateWithClusters(TableClusteringMainEngine mainEngine, Integer numberOfClusters) {
		
		mainEngine.extractClusters(numberOfClusters);
		setClusterCollectors(mainEngine.getClusterCollectors());
		mainEngine.print();
	}
	
	public String getClusterDescription(String description, int row) {
		
		Cluster cluster = getClusterCollectors().get(0).getClusters().get(row);
		
		description = description + "Birth Version Name:" + cluster.getBirthSqlFile() + "\n";
		description = description + "Birth Version ID:" + cluster.getBirth() + "\n";
		description = description + "Death Version Name:" + cluster.getDeathSqlFile() + "\n";
		description = description + "Death Version ID:" + cluster.getDeath() + "\n";
		description = description + "Tables:" + cluster.getNamesOfTables().size() + "\n";
		description = description + "Total Changes:" + cluster.getTotalChanges() + "\n";
		
		return description;
		
	}
	
	public int getNamesOfTables(int row) {
		
		Cluster cluster = getClusterCollectors().get(0).getClusters().get(row);
		
		return cluster.getNamesOfTables().size();
	}
	
	public TableConstructionWithClusters createTableConstructionWithClusters() {
		
		return new TableConstructionWithClusters(getPhaseCollectors().get(0).getPhases(),
				getClusterCollectors().get(0).getClusters());
	}

}
