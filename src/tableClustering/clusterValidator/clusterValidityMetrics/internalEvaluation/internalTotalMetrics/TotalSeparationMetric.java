package tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalTotalMetrics;

import java.util.ArrayList;
import java.util.Iterator;

import tableClustering.clusterValidator.commons.ClusterInfoKeeper;

public class TotalSeparationMetric implements InternalTotalMetrics {

	private ArrayList<ClusterInfoKeeper> clusterInfoKeepers = new ArrayList<ClusterInfoKeeper>();
	private Double totalSeparation=null;
	
	public TotalSeparationMetric(ArrayList<ClusterInfoKeeper> clusterInfoKeepers) {
	
		this.clusterInfoKeepers=clusterInfoKeepers;
		
	}
	
	@Override
	public void compute(){
		
		Iterator<ClusterInfoKeeper> iteratorClusterInfoKeeper = clusterInfoKeepers.iterator();
		totalSeparation = new Double(0);

 		while(iteratorClusterInfoKeeper.hasNext()){
			
			ClusterInfoKeeper currClusterInfoKeeper = iteratorClusterInfoKeeper.next();
			
			totalSeparation= totalSeparation + currClusterInfoKeeper.getClusterSeparation();
		}
		System.err.println("Total Separation"+totalSeparation);

	}
		
	@Override
	public Double getResult() {

		return totalSeparation;
	}

}