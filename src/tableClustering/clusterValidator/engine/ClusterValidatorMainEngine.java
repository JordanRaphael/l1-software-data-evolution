package tableClustering.clusterValidator.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;
import tableClustering.clusterValidator.clusterValidityMetrics.totalMetrics.TotalCohesionMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.totalMetrics.TotalSeparationMetric;
import tableClustering.clusterValidator.commons.Centroid;
import tableClustering.clusterValidator.commons.ClusterInfoKeeper;
import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLTable;

public class ClusterValidatorMainEngine {
	
	private GlobalDataKeeper globalDataKeeper=null;
	private ArrayList<ClusterInfoKeeper> clusterInfoKeepers= new ArrayList<ClusterInfoKeeper>();
	private Centroid overallCentroid = null;
	private Double totalCohesion = null;
	private Double totalSeparation = null;
	
	public ClusterValidatorMainEngine(GlobalDataKeeper globalDataKeeper){
		this.globalDataKeeper=globalDataKeeper;
		initialize();

	}
	
	public void run(){

		TotalCohesionMetric totalCohesionMetricCalculator = new TotalCohesionMetric(clusterInfoKeepers);
		totalCohesionMetricCalculator.compute();
		totalCohesion=totalCohesionMetricCalculator.getResult();
		
		TotalSeparationMetric totalSeparationMetricCalculator = new TotalSeparationMetric(clusterInfoKeepers);
		totalSeparationMetricCalculator.compute();
		totalSeparation=totalSeparationMetricCalculator.getResult();
		
	}

	private void initialize() {
		
		initializeOverallCentroid();
		initializeClusterInfoKeepers();
		totalCohesion = new Double(0);
		totalSeparation = new Double(0);
	}

	private void initializeClusterInfoKeepers() {
		ArrayList<Cluster> clusters = globalDataKeeper.getClusterCollectors().get(0).getClusters();
		
		Iterator<Cluster> clusterIterator = clusters.iterator();
		
		while(clusterIterator.hasNext()){
			
			ClusterInfoKeeper clusterInfoKeeper = new ClusterInfoKeeper(clusterIterator.next(),overallCentroid);
			clusterInfoKeepers.add(clusterInfoKeeper);
		}
	}
	
	private void initializeOverallCentroid(){
		
		TreeMap<String, PPLTable> tables= globalDataKeeper.getAllPPLTables();
		double x=0;
		double y=0;
		double z=0;
		for(Map.Entry<String,PPLTable> pplTab:tables.entrySet()){
			x = x +pplTab.getValue().getBirthVersionID();
			y = y+pplTab.getValue().getDeathVersionID();
			z= z+pplTab.getValue().getTotalChanges();
		}
		
		x= x/tables.size();
		y= y/tables.size();
		z= z/tables.size();
		
		this.overallCentroid=new Centroid(x, y, z);
		
		System.out.println(this.overallCentroid.getX()+" "+this.overallCentroid.getY()+" "+this.overallCentroid.getZ());
		
	}
	
	public Double getTotalCohesion() {
		return totalCohesion;
	}

	public Double getTotalSeparation() {
		return totalSeparation;
	}
	
	
	
}
