package tableClustering.analysis;

import java.util.TreeMap;

import tableClustering.commons.ClusterCollector;
import data.pplSqlSchema.PPLTable;


public interface ClusterExtractor {

	public ClusterCollector extractAtMostKClusters(TreeMap<String, PPLTable> tables,
			int numClusters,float birthWeight,float deathWeight, float changeWeight );
	
}
