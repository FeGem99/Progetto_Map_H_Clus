package clustering;
//da sistemare
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import data.Data;
import distance.ClusterDistance;

public class HierachicalClusterMiner implements Serializable {
	
	private static final long serialVersionUID = 1L;
    private Dendrogram dendrogram;

	
	
	
	public HierachicalClusterMiner(int depth) {
		dendrogram= new Dendrogram(depth);
	
	}
	
	
        @Override
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

    //metodo per salvare un'istanza serializzata su file
    public static HierachicalClusterMiner loadHierachicalClusterMiner(String fileName, Data data)
            throws FileNotFoundException, IOException, ClassNotFoundException, InvalidDepthException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            HierachicalClusterMiner miner = (HierachicalClusterMiner) in.readObject();

            // Verifica la validità della profondità del dendrogramma caricato
            int numberOfExample = data.getNumberOfExample();
            if (miner.dendrogram.getDepth() > numberOfExample) {
                throw new InvalidDepthException("La profondità del dendrogramma caricato è maggiore del numero di esempi nel dataset.");
            }

            return miner;
        }
    }

    public void save(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this); // Serializza l'oggetto corrente
        }
    }
}
