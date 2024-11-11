package clustering;

import java.io.Serializable;

import data.Data;
import distance.ClusterDistance;

/**
 * La classe ClusterSet rappresenta un insieme di cluster. Gestisce un array di cluster e fornisce metodi
 * per aggiungere, ottenere e visualizzare i cluster. Permette anche di unire i due cluster più simili.
 */
class ClusterSet implements Serializable {

    /** Array che contiene i cluster nel set. */
    private Cluster C[];
    
    /** Indice dell'ultimo cluster aggiunto nel set. */
    private int lastClusterIndex = 0;

    /**
     * Costruttore della classe ClusterSet. Inizializza un array di cluster con la dimensione specificata.
     * 
     * @param k il numero di cluster che il set può contenere.
     */
    ClusterSet(int k) {
        C = new Cluster[k];
    }

    /**
     * Aggiunge un cluster al ClusterSet se non è già presente.
     * 
     * @param c il cluster da aggiungere.
     */
    void add(Cluster c) {
        // Verifica se il cluster è già presente nel set
        for (int j = 0; j < lastClusterIndex; j++)
            if (c == C[j]) 
                return;
        C[lastClusterIndex] = c;
        lastClusterIndex++;
    }

    /**
     * Restituisce il cluster all'indice specificato.
     * 
     * @param i l'indice del cluster da restituire.
     * @return il cluster all'indice i.
     */
    Cluster get(int i) {
        return C[i];
    }

    /**
     * Restituisce una rappresentazione in forma di stringa di tutti i cluster nel set.
     * Ogni cluster è rappresentato dalla sua posizione nell'array.
     * 
     * @return una stringa contenente la rappresentazione di tutti i cluster.
     */
    public String toString() {
        String str = "";
        for (int i = 0; i < C.length; i++) {
            if (C[i] != null) {
                str += "cluster" + i + ":" + C[i] + "\n";
            }
        }
        return str;
    }

    /**
     * Restituisce una rappresentazione in forma di stringa di tutti i cluster nel set,
     * utilizzando un oggetto Data per ottenere la rappresentazione degli esempi contenuti
     * in ogni cluster.
     * 
     * @param data l'oggetto Data che fornisce la rappresentazione degli esempi.
     * @return una stringa contenente la rappresentazione di tutti i cluster con i dati.
     */
    String toString(Data data) {
        String str = "";
        for (int i = 0; i < C.length; i++) {
            if (C[i] != null) {
                str += "cluster" + i + ":" + C[i].toString(data) + "\n";
            }
        }
        return str;
    }

    /**
     * Determina la coppia di cluster più simili utilizzando l'oggetto ClusterDistance e fonde
     * i due cluster in uno solo, restituendo un nuovo ClusterSet con i cluster uniti.
     * 
     * @param distance l'oggetto per calcolare la distanza tra i cluster.
     * @param data l'oggetto che rappresenta il dataset da utilizzare nel calcolo della distanza.
     * @return un nuovo ClusterSet con i cluster fusi.
     * @throws IllegalArgumentException se ci sono meno di 2 cluster nel set.
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
            } else if (i == cluster1Index) {
                newClusterSet.add(mergedCluster);
            }
        }
        return newClusterSet;
    }
}
