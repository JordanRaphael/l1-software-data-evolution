package tableClustering.analysis;



public class ClusterExtractorFactory {
	
	public ClusterExtractor createClusterExtractor(String concreteClassName){
		if (concreteClassName.equals("AgglomerativeClusterExtractor")){
			return new AgglomerativeClusterExtractor();
		}
/*
 * 		else if (concreteClassName.equals("ANOTHERNAME")){
			return new ANOTHERNAME();
		}
		else ... and so on 
 */
		System.out.println("If the code got up to here, you passed a wrong argument to the PhaseExtractorFactory");
		return null;
	}

}
