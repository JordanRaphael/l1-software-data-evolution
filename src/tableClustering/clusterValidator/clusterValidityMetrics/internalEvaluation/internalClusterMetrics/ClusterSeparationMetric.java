package tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalClusterMetrics;

import tableClustering.clusterValidator.commons.Centroid;

public class ClusterSeparationMetric implements InternalClusterMetrics {

	private Double clusterSeparation = null;
	private Centroid overallCentroid = null;
	private Centroid clusterCentroid = null;

	public ClusterSeparationMetric(Centroid clusterCentroid, Centroid overallCentroid) {

		this.clusterCentroid = clusterCentroid;
		this.overallCentroid = overallCentroid;

	}

	@Override
	public void computeMetric() {

		clusterSeparation = new Double(0);

		Double distanceX = null;
		Double distanceY = null;
		Double distanceZ = null;

		distanceX = new Double(Math.pow((double) (clusterCentroid.getX() - overallCentroid.getX()), 2.0));

		distanceY = new Double(Math.pow((double) (clusterCentroid.getY() - overallCentroid.getY()), 2.0));

		distanceZ = new Double(Math.pow((double) (clusterCentroid.getZ() - overallCentroid.getZ()), 2.0));

		// Euclidean Distance
		clusterSeparation = Math.sqrt(distanceX + distanceY + distanceZ);

	}

	@Override
	public Double getResult() {
		return clusterSeparation;
	}

}