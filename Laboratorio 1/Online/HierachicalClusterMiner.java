

class HierachicalClusterMiner {
	
	private Dendrogram dendrogram;

	
	
	
	HierachicalClusterMiner(int depth) {
		dendrogram= new Dendrogram(depth);
	
	}
	
	
	public String toString() {
		return dendrogram.toString();
	}
	
	String toString(Data data) {
		return dendrogram.toString(data);
	}
	
	void mine(Data data, ClusterDistance distance) {
		int numberOfExamples = data.getNumberOfExample();
		
		// Livello 0: ogni esempio è un cluster separato
		ClusterSet level0 = new ClusterSet(numberOfExamples);
		for (int i = 0; i < numberOfExamples; i++) {
			Cluster cluster = new Cluster();  // Creiamo un cluster vuoto
			cluster.addData(i);  // Aggiungiamo l'indice dell'esempio al cluster
			level0.add(cluster);  // Aggiungiamo il cluster al ClusterSet di livello 0
		}
		dendrogram.setClusterSet(level0, 0); // Memorizziamo il livello 0 nel dendrogramma
	
		// Costruzione dei livelli successivi
		for (int level = 1; level < dendrogram.tree.length; level++) {
			ClusterSet previousLevel = dendrogram.getClusterSet(level - 1);
			if (previousLevel == null) {
				throw new IllegalStateException("Livello precedente nel dendrogramma è nullo");
			}
			
			ClusterSet newLevel = previousLevel.mergeClosestClusters(distance, data);  // Fusione dei cluster più vicini
			if (newLevel == null) {
				throw new IllegalStateException("La fusione dei cluster ha restituito un ClusterSet nullo");
			}
			
			dendrogram.setClusterSet(newLevel, level); // Memorizziamo il nuovo livello nel dendrogramma
		}
	}
	
}
