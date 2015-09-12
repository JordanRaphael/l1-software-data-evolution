package tableClustering.clusterValidator.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalClusterMetrics.ExternalClusterMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalTotalMetrics.ExternalTotalMetrics;
import tableClustering.clusterValidator.clusterValidityMetrics.externalEvaluation.externalTotalMetrics.TotalEntropyMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalTotalMetrics.InternalTotalMetrics;
import tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalTotalMetrics.TotalCohesionMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.internalEvaluation.internalTotalMetrics.TotalSeparationMetric;
import tableClustering.clusterValidator.commons.Centroid;
import tableClustering.clusterValidator.commons.ClassOfObjects;
import tableClustering.clusterValidator.commons.ClusterInfoKeeper;
import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLTable;

public class ClusterValidatorMainEngine {
	
	private GlobalDataKeeper globalDataKeeper=null;
	private ArrayList<ClusterInfoKeeper> clusterInfoKeepers= new ArrayList<ClusterInfoKeeper>();
	private Centroid overallCentroid = null;
	private Double totalCohesion = null;
	private Double totalSeparation = null;
	private Double totalEntropy = null;

	private ArrayList<ClassOfObjects> classesOfObjects = new ArrayList<ClassOfObjects>();
	
	public ClusterValidatorMainEngine(GlobalDataKeeper globalDataKeeper) throws IOException{
		this.globalDataKeeper=globalDataKeeper;
		initialize();

	}
	
	public void run(){

		InternalTotalMetrics totalCohesionMetricCalculator = new TotalCohesionMetric(clusterInfoKeepers);
		totalCohesionMetricCalculator.compute();
		totalCohesion=totalCohesionMetricCalculator.getResult();
		
		InternalTotalMetrics totalSeparationMetricCalculator = new TotalSeparationMetric(clusterInfoKeepers);
		totalSeparationMetricCalculator.compute();
		totalSeparation=totalSeparationMetricCalculator.getResult();
		
		
		ExternalTotalMetrics totalEntropyCalculator = new TotalEntropyMetric(clusterInfoKeepers, globalDataKeeper.getAllPPLTables().size());
		totalEntropyCalculator.compute();
		totalEntropy = totalEntropyCalculator.getResult();
		
	}

	private void initialize() throws IOException {
		
		initializeOverallCentroid();
		initializeClassesOfObjects();
		initializeClusterInfoKeepers();
		totalCohesion = new Double(0);
		totalSeparation = new Double(0);
		totalEntropy = new Double(0);
	}

	private void initializeClusterInfoKeepers() {
		ArrayList<Cluster> clusters = globalDataKeeper.getClusterCollectors().get(0).getClusters();
		
		Iterator<Cluster> clusterIterator = clusters.iterator();
		int classIndex =0;
		while(clusterIterator.hasNext()){
			
			ClusterInfoKeeper clusterInfoKeeper = new ClusterInfoKeeper(clusterIterator.next(),overallCentroid);
			clusterInfoKeeper.computeClusterEntropy(classesOfObjects, clusters, classIndex);
			clusterInfoKeepers.add(clusterInfoKeeper);
			
			classIndex++;
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
	
	private void initializeClassesOfObjects() throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader("filesHandler/input/classesForValidity.csv"));
		//File f = new File("input/classesForValidity.csv");
	
		
		String line;
		ClassOfObjects classToAdd=null;
		ArrayList<String> objectsOfClass = new ArrayList<String>();
		while((line = br.readLine()) != null) {
			//line = br.readLine();
			//String[] splitLine = line.split(";");
			if(line.contains("Class ")){
				if (classToAdd!=null) {
					classToAdd.setObjects(objectsOfClass);
					classesOfObjects.add(classToAdd);
				}
				
				
				classToAdd = new ClassOfObjects(line);
				objectsOfClass = new ArrayList<String>();
				
			}
			else{
				objectsOfClass.add(line);
			}

		}
		
		classToAdd.setObjects(objectsOfClass);
		classesOfObjects.add(classToAdd);

		br.close();
		
		for(int i=0; i<classesOfObjects.size(); i++){
			System.out.println(classesOfObjects.get(i).toString());
		}
		
	}
	
	public Double getTotalCohesion() {
		return totalCohesion;
	}

	public Double getTotalSeparation() {
		return totalSeparation;
	}
	
	public Double getTotalEntropy() {
		return totalEntropy;
	}
	
	
	
}
