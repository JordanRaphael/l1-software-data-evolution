package tableClustering.clusterExtractor.analysis;

import tableClustering.clusterExtractor.commons.ClusterCollector;
import data.dataKeeper.GlobalDataManager;

public interface ClusterExtractor {

	public ClusterCollector extractAtMostKClusters(GlobalDataManager dataKeeper, int numClusters, Double birthWeight,
			Double deathWeight, Double changeWeight);

}
