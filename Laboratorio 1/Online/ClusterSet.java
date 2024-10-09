

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
	
	/**
     * Determina la coppia di cluster più simili utilizzando l'oggetto distance e fonde
     * i due cluster in uno solo.
     *
     * @param distance l'oggetto per calcolare la distanza tra i cluster.
     * @param data l'oggetto che rappresenta il dataset.
     * @return una nuova istanza di ClusterSet con i cluster fusi.
     */
    ClusterSet mergeClosestClusters(ClusterDistance distance, Data data) {
        if (lastClusterIndex < 2) {
            throw new IllegalArgumentException("Non ci sono abbastanza cluster per eseguire la fusione.");
        }

        double minDistance = Double.MAX_VALUE;
        int cluster1Index = -1;
        int cluster2Index = -1;

        // Trova la coppia di cluster con la minima distanza
        for (int i = 0; i < lastClusterIndex; i++) {
            for (int j = i + 1; j < lastClusterIndex; j++) {
                double dist = distance.distance(C[i], C[j], data);
                if (dist < minDistance) {
                    minDistance = dist;
                    cluster1Index = i;
                    cluster2Index = j;
                }
            }
        }

        // Fonde i due cluster
        Cluster mergedCluster = C[cluster1Index].mergeCluster(C[cluster2Index]);

        // Crea un nuovo ClusterSet con un cluster in meno
        ClusterSet newClusterSet = new ClusterSet(lastClusterIndex - 1);
        for (int i = 0; i < lastClusterIndex; i++) {
            if (i != cluster1Index && i != cluster2Index) {
                newClusterSet.add(C[i]);
            }else if(i == cluster1Index) {
            	newClusterSet.add(mergedCluster);
            }
        }
        return newClusterSet;
    }
}
