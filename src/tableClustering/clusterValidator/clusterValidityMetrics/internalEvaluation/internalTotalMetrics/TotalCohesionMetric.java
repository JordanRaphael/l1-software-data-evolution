package tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalTotalMetrics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLTable;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterValidator.commons.Centroid;
import tableClustering.clusterValidator.commons.ClusterInfoKeeper;

public class TotalCohesionMetric implements InternalTotalMetrics {

	private ArrayList<ClusterInfoKeeper> clusterInfoKeepers = new ArrayList<ClusterInfoKeeper>();
	private Double totalCohesion=null;
	
	public TotalCohesionMetric(ArrayList<ClusterInfoKeeper> clusterInfoKeepers) {
	
		this.clusterInfoKeepers=clusterInfoKeepers;
		
	}
	
	@Override
	public void compute(){
		
		Iterator<ClusterInfoKeeper> iteratorClusterInfoKeeper = clusterInfoKeepers.iterator();
		totalCohesion = new Double(0);

		// totalCohesion = Sum1-K(wi*validity(Ci))
 		while(iteratorClusterInfoKeeper.hasNext()){
			
			ClusterInfoKeeper currClusterInfoKeeper = iteratorClusterInfoKeeper.next();
			
			totalCohesion= totalCohesion + currClusterInfoKeeper.getClusterCohesion();
			
		}
		//System.err.println(totalCohesion);

	}

	
	
	@Override
	public Double getResult() {
		
		return this.totalCohesion;
	}

}
