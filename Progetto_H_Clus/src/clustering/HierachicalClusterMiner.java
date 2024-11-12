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
 * La classe HierachicalClusterMiner gestisce il processo di clustering gerarchico,
 * che costruisce un dendrogramma a partire da un dataset. I cluster vengono 
 * uniti iterativamente in base alla distanza tra di loro fino a formare un dendrogramma
 * completo con una profondità specificata.
 */
public class HierachicalClusterMiner implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Il dendrogramma che rappresenta l'albero gerarchico dei cluster.
     */
    private Dendrogram dendrogram;

    /**
     * Costruisce un miner per il clustering gerarchico con una profondità specificata.
     * 
     * @param depth la profondità del dendrogramma da costruire.
     */
    public HierachicalClusterMiner(int depth) {
        dendrogram = new Dendrogram(depth);
    }

    /**
     * Restituisce una rappresentazione in formato stringa del dendrogramma.
     * 
     * @return una stringa che rappresenta l'intero dendrogramma.
     */
    @Override
    public String toString() {
        return dendrogram.toString();
    }

    /**
     * Restituisce una rappresentazione in formato stringa del dendrogramma, 
     * includendo i dati associati ai cluster.
     * 
     * @param data l'oggetto Data utilizzato per ottenere gli esempi associati ai cluster.
     * @return una stringa che rappresenta l'intero dendrogramma con i dati.
     */
    public String toString(Data data) {
        return dendrogram.toString(data);
    }

    /**
     * Esegue il clustering gerarchico utilizzando il dataset e la funzione di distanza specificata.
     * Questo metodo costruisce il dendrogramma iterativamente, unendo i cluster più vicini
     * fino a raggiungere la profondità desiderata.
     * 
     * @param data il dataset su cui eseguire il clustering.
     * @param distance la funzione di distanza utilizzata per determinare la somiglianza tra i cluster.
     * @throws InvalidDepthException se la profondità del dendrogramma è maggiore del numero di esempi nel dataset.
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
     * @param fileName il nome del file da cui caricare l'istanza.
     * @param data il dataset da utilizzare per verificare la validità della profondità.
     * @return l'oggetto HierachicalClusterMiner caricato.
     * @throws FileNotFoundException se il file non viene trovato.
     * @throws IOException se si verifica un errore durante la lettura del file.
     * @throws ClassNotFoundException se la classe HierachicalClusterMiner non è trovata.
     * @throws InvalidDepthException se la profondità del dendrogramma nel file è maggiore del numero di esempi nel dataset.
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
     * Salva l'istanza corrente di HierachicalClusterMiner su un file.
     * 
     * @param filePath il percorso del file in cui salvare l'istanza.
     * @throws IOException se si verifica un errore durante il salvataggio dell'oggetto.
     */
    public void save(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this); // Serializza l'oggetto corrente
        }
    }
}
