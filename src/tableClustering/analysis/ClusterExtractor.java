package tableClustering.analysis;

import tableClustering.commons.ClusterCollector;
import data.dataKeeper.GlobalDataKeeper;


public interface ClusterExtractor {

	public ClusterCollector extractAtMostKClusters(GlobalDataKeeper dataKeeper,
			int numClusters,float birthWeight,float deathWeight, float changeWeight );
	
}
