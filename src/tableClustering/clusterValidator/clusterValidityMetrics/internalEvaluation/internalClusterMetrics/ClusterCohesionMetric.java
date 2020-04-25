package tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalClusterMetrics;

import java.util.Map;
import java.util.TreeMap;

import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterValidator.commons.Centroid;
import tableClustering.clusterValidator.commons.ClusterInfoKeeper;
import data.dataPPL.pplSQLSchema.PPLTable;

public class ClusterCohesionMetric implements InternalClusterMetrics {
	
	ClusterInfoKeeper clusterInfoKeeper = null;
	private Double sumClusterCohesion = null;
	
	public ClusterCohesionMetric(ClusterInfoKeeper clusterInfoKeeper){
		
		this.clusterInfoKeeper=clusterInfoKeeper;
		
	}
	
	@Override
	public void computeMetric() {
		Cluster currCluster = clusterInfoKeeper.getCluster();
		TreeMap<String, PPLTable> currClusterTables = currCluster.getTables();
		Centroid clusterCentroid = clusterInfoKeeper.getCentroid();
		
		sumClusterCohesion = new Double(0);
		
		for(Map.Entry<String,PPLTable> pplTab:currClusterTables.entrySet()){
			sumClusterCohesion = sumClusterCohesion+computeDistanceFromDataPointToCentroid(pplTab.getValue(),clusterCentroid);
		}
	}
	
	private Double computeDistanceFromDataPointToCentroid(PPLTable tableToComputeDistance, Centroid centroidOfCluster){
		
		Double distance = null;
		
		Double distanceX = null;
		Double distanceY = null;
		Double distanceZ = null;

		distanceX=Math.pow((double)(tableToComputeDistance.getBirthVersionID()-centroidOfCluster.getX()),2.0);
		distanceY=Math.pow((double)(tableToComputeDistance.getDeathVersionID()-centroidOfCluster.getY()),2.0);
		distanceZ=Math.pow((double)(tableToComputeDistance.getTotalChanges()-centroidOfCluster.getZ()),2.0);

		//Euclidean Distance
		distance=Math.sqrt(distanceX+distanceY+distanceZ);
		
		return distance;
		
	}
	
	@Override
	public Double getResult(){
		return sumClusterCohesion;
	}

}