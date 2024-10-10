package clustering;

import data.Data;
import distance.ClusterDistance;

public class HierachicalClusterMiner {
	
	private Dendrogram dendrogram;

	
	
	
	public HierachicalClusterMiner(int depth) {
		dendrogram= new Dendrogram(depth);
	
	}
	
	
	public String toString() {
		return dendrogram.toString();
	}
	
	public String toString(Data data) {
		return dendrogram.toString(data);
	}
	
	public void mine(Data data, ClusterDistance distance) throws InvalidDepthException{
        // Verifica se la profondità specificata è valida
        int numberOfExample = data.getNumberOfExample();
        if (dendrogram.getDepth() > numberOfExample) {
            throw new InvalidDepthException("La profondità del dendrogramma è maggiore del numero di esempi nel dataset.");
        }

        // Step 1: Crea il livello base (livello 0) del dendrogramma
        ClusterSet level0 = new ClusterSet(numberOfExample);
        for (int i = 0; i < numberOfExample; i++) {
            Cluster cluster = new Cluster();
            cluster.addData(i);
            level0.add(cluster);
        }
        dendrogram.setClusterSet(level0, 0);

        // Step 2: Costruisci i livelli successivi del dendrogramma
        for (int level = 1; level < dendrogram.getDepth(); level++) {
            ClusterSet prevLevel = dendrogram.getClusterSet(level - 1);
            ClusterSet newLevel = prevLevel.mergeClosestClusters(distance, data);
            dendrogram.setClusterSet(newLevel, level);
        }
    }
}

