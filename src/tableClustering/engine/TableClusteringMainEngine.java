package tableClustering.engine;

import java.util.ArrayList;

import tableClustering.analysis.ClusterExtractor;
import tableClustering.analysis.ClusterExtractorFactory;
import tableClustering.commons.ClusterCollector;
import data.dataKeeper.GlobalDataKeeper;

public class TableClusteringMainEngine {
	
	private GlobalDataKeeper dataKeeper;
	private Float birthWeight;
	private Float deathWeight;
	private Float changeWeight;
	private ArrayList<ClusterCollector> clusterCollectors;
	private ClusterExtractorFactory clusterExtractorFactory;
	private ClusterExtractor clusterExtractor;
	private ArrayList<ClusterCollector> allClusterCollectors;



	public TableClusteringMainEngine(GlobalDataKeeper dataKeeper,Float birthWeight, Float deathWeight,
			Float changeWeight){
		
		this.dataKeeper=dataKeeper;
		this.birthWeight=birthWeight;
		this.deathWeight=deathWeight;
		this.changeWeight=changeWeight;
		
		clusterExtractorFactory = new ClusterExtractorFactory();
		clusterExtractor = clusterExtractorFactory.createClusterExtractor("AgglomerativeClusterExtractor");
		
		allClusterCollectors = new ArrayList<ClusterCollector>();

		
	}
	
	public void extractClusters(int numClusters){
		//report=new String("");
		clusterCollectors = new ArrayList<ClusterCollector>();
		
		ClusterCollector clusterCollector = new ClusterCollector();
		clusterCollector = clusterExtractor.extractAtMostKClusters(dataKeeper, numClusters, birthWeight, deathWeight, changeWeight);
		clusterCollectors.add(clusterCollector);
		
		allClusterCollectors.add(clusterCollector);

	}
	
	public void print(){
		
		String toPrint="";
		
		for(int i=0; i<allClusterCollectors.size(); i++){
			ClusterCollector clusterCollector=allClusterCollectors.get(i);
			toPrint=toPrint+clusterCollector.toString();
			
		}
		
		System.out.println(toPrint);
	}
	
}
