package clustering;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import data.Data;
import distance.ClusterDistance;

/**
 * La classe HierachicalClusterMiner è un'implementazione di un miner per clustering gerarchico.
 * Gestisce la costruzione di un dendrogramma attraverso il processo di fusione dei cluster.
 */
public class HierachicalClusterMiner implements Serializable {

    /** SerialVersionUID per garantire la compatibilità nella serializzazione. */
    private static final long serialVersionUID = 1L;

    /** Rappresenta il dendrogramma che modella i cluster gerarchici. */
    private Dendrogram dendrogram;

    /**
     * Costruttore della classe HierachicalClusterMiner. Crea un dendrogramma con la profondità specificata.
     * 
     * @param depth la profondità del dendrogramma.
     */
    public HierachicalClusterMiner(int depth) {
        dendrogram = new Dendrogram(depth);
    }

    /**
     * Restituisce una rappresentazione in forma di stringa del dendrogramma.
     * 
     * @return una stringa che rappresenta il dendrogramma.
     */
    @Override
    public String toString() {
        return dendrogram.toString();
    }

    /**
     * Restituisce una rappresentazione in forma di stringa del dendrogramma utilizzando i dati forniti.
     * 
     * @param data l'oggetto Data che fornisce la rappresentazione degli esempi.
     * @return una stringa che rappresenta il dendrogramma, con i dati degli esempi.
     */
    public String toString(Data data) {
        return dendrogram.toString(data);
    }

    /**
     * Esegue il mining del clustering gerarchico sui dati forniti, costruendo il dendrogramma.
     * 
     * @param data l'oggetto Data che contiene gli esempi da raggruppare.
     * @param distance l'oggetto ClusterDistance per calcolare la distanza tra i cluster.
     * @throws InvalidDepthException se la profondità del dendrogramma è maggiore del numero di esempi.
     */
    public void mine(Data data, ClusterDistance distance) throws InvalidDepthException {
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

    /**
     * Carica un'istanza serializzata di HierachicalClusterMiner da un file.
     * 
     * @param fileName il percorso del file da cui caricare l'oggetto.
     * @param data l'oggetto Data utilizzato per verificare la profondità del dendrogramma caricato.
     * @return l'istanza di HierachicalClusterMiner caricata.
     * @throws FileNotFoundException se il file non viene trovato.
     * @throws IOException se si verifica un errore durante la lettura del file.
     * @throws ClassNotFoundException se la classe dell'oggetto caricato non viene trovata.
     * @throws InvalidDepthException se la profondità del dendrogramma caricato è maggiore del numero di esempi nel dataset.
     */
    public static HierachicalClusterMiner loadHierachicalClusterMiner(String fileName, Data data)
            throws FileNotFoundException, IOException, ClassNotFoundException, InvalidDepthException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            HierachicalClusterMiner miner = (HierachicalClusterMiner) in.readObject();

            // Se data è null, non possiamo verificare la profondità
            if (data != null) {
                int numberOfExample = data.getNumberOfExample();
                if (miner.dendrogram.getDepth() > numberOfExample) {
                    throw new InvalidDepthException("La profondità del dendrogramma caricato è maggiore del numero di esempi nel dataset.");
                }
            }

            return miner;
        }
    }

    /**
     * Salva l'istanza corrente di HierachicalClusterMiner in un file.
     * 
     * @param filePath il percorso del file in cui salvare l'oggetto.
     * @throws IOException se si verifica un errore durante il salvataggio.
     */
    public void save(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this); // Serializza l'oggetto corrente
        }
    }
}
