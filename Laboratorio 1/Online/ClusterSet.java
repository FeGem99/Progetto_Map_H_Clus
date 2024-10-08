
class ClusterSet {

	private Cluster C[];
	private int lastClusterIndex=0;
	
	ClusterSet(int k){
		C=new Cluster[k];
	}
	
	void add(Cluster c){
		for(int j=0;j<lastClusterIndex;j++)
			if(c==C[j]) // to avoid duplicates
				return;
		C[lastClusterIndex]=c;
		lastClusterIndex++;
	}
	
	Cluster get(int i){
		return C[i];
	}
	
	
	
	public String toString(){
		String str="";
		for(int i=0;i<C.length;i++){
			if (C[i]!=null){
				str+="cluster"+i+":"+C[i]+"\n";
		
			}
		}
		return str;
		
	}

	
	String toString(Data data){
		String str="";
		for(int i=0;i<C.length;i++){
			if (C[i]!=null){
				str+="cluster"+i+":"+C[i].toString(data)+"\n";
		
			}
		}
		return str;
		
	}
	
	ClusterSet mergeClosestClusters(ClusterDistance distance, Data data) {
		// controllare il numero minimo di cluster per effettuare la fusione 
		if (lastClusterIndex<2) {
			return this; 
		} else {
			int closestCluster1 = -1;
			int closestCluster2 = -1;
			double minDistance = Double.MAX_VALUE;
			for (int i=0; i<lastClusterIndex; i++){
				for (int j=0; j<lastClusterIndex; j++) {
					double dist = distance.distance(C[i], C[j], data);
					if(dist < minDistance){
						minDistance= dist;
						closestCluster1 = i;
						closestCluster2 = j; 
					}
				}
			}
		//nuovo cluster che risulta la fusione dei due cluster più vicini
		Cluster mergedCluster = C[closestCluster1].mergeCluster(C[closestCluster2]);
		//creo un cluster nuovo di dimensione Clusterset-1
		ClusterSet newClusterSet= new ClusterSet(lastClusterIndex-1);

		for(int i=1; i<lastClusterIndex; i++){
			if (i != closestCluster1 && i!= closestCluster2) {
				newClusterSet.add(C[i]);
			}
		}
		
		newClusterSet.add(mergedCluster);
		
		return newClusterSet;
	}
	}
		

}
