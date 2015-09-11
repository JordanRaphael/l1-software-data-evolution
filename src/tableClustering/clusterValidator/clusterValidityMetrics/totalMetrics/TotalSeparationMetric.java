package tableClustering.clusterValidator.clusterValidityMetrics.totalMetrics;

import java.util.ArrayList;
import java.util.Iterator;

import tableClustering.clusterValidator.commons.ClusterInfoKeeper;

public class TotalSeparationMetric implements TotalMetrics {

	private ArrayList<ClusterInfoKeeper> clusterInfoKeepers = new ArrayList<ClusterInfoKeeper>();
	private Double totalSeparation=null;
	
	public TotalSeparationMetric(ArrayList<ClusterInfoKeeper> clusterInfoKeepers) {
	
		this.clusterInfoKeepers=clusterInfoKeepers;
		
	}
	
	@Override
	public void compute(){
		
		Iterator<ClusterInfoKeeper> iteratorClusterInfoKeeper = clusterInfoKeepers.iterator();
		totalSeparation = new Double(0);

		// totalSeparation = Sum1-K(mi*validity(Ci)) , mi= datapoints that are represented by cluster centroid
 		while(iteratorClusterInfoKeeper.hasNext()){
			
			ClusterInfoKeeper currClusterInfoKeeper = iteratorClusterInfoKeeper.next();
			
			totalSeparation= totalSeparation + currClusterInfoKeeper.getClusterSeparation();
		}
		System.err.println(totalSeparation);

	}
	
	
	@Override
	public Double getResult() {
		// TODO Auto-generated method stub
		return totalSeparation;
	}

	
}
