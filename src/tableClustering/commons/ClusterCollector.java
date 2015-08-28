package tableClustering.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ClusterCollector {

	private ArrayList<Cluster> clusters;
	
	public ClusterCollector(){
		clusters=new ArrayList<Cluster>();
	}

	
	public void addCluster(Cluster c){
		this.clusters.add(c);
	}
	
	public void sortClustersByBirth(){
		
		 Collections.sort(clusters, new Comparator<Cluster>() {
		        @Override
		        public int compare(final Cluster object1, final Cluster object2) {
		            return Integer.compare(object1.getBirth(),object2.getBirth());
		        }
		       } );
		 
		 
	}
		
		
	
	
	public ArrayList<Cluster> getClusters(){
		return clusters;
	}
	
	public void setClusters(ArrayList<Cluster> clusters) {
		this.clusters = clusters;
	}
	
	public String toString(){
		
		String toReturn="";
		for(int i=0; i<this.clusters.size();i++){
			
			toReturn=toReturn+this.clusters.get(i).toString();
		}
		
		return toReturn;
	}
	
}
