package clustering;

import java.io.Serializable;
import data.Data;
import distance.ClusterDistance;

/**
 * La classe ClusterSet rappresenta un insieme di cluster.
 * Essa gestisce una collezione di oggetti di tipo `Cluster` e fornisce metodi
 * per aggiungere, ottenere e rappresentare come stringa i cluster. Include anche
 * un metodo per fondere i due cluster più simili.
 */
class ClusterSet implements Serializable {

    /**
     * Un array che memorizza i cluster contenuti nell'insieme.
     */
    private Cluster C[];

    /**
     * L'indice dell'ultimo cluster aggiunto nell'array.
     */
    private int lastClusterIndex = 0;

    /**
     * Costruisce un nuovo ClusterSet con un numero specificato di cluster.
     * 
     * @param k il numero di cluster iniziali nel set.
     */
    ClusterSet(int k) {
        C = new Cluster[k];
    }

    /**
     * Aggiunge un cluster all'insieme, evitando duplicati.
     * 
     * @param c il cluster da aggiungere.
     */
    void add(Cluster c) {
        for (int j = 0; j < lastClusterIndex; j++)
            if (c == C[j]) // per evitare duplicati
                return;
        C[lastClusterIndex] = c;
        lastClusterIndex++;
    }

    /**
     * Restituisce il cluster all'indice specificato nell'insieme.
     * 
     * @param i l'indice del cluster da ottenere.
     * @return il cluster all'indice specificato.
     */
    Cluster get(int i) {
        return C[i];
    }

    /**
     * Restituisce una rappresentazione in formato stringa di tutti i cluster nel set.
     * Ogni cluster è identificato dal suo indice e rappresentato con il metodo
     * `toString` del cluster.
     * 
     * @return una stringa che rappresenta tutti i cluster nel set.
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
     * Restituisce una rappresentazione in formato stringa di tutti i cluster nel set,
     * con gli esempi associati agli indici del dataset. Ogni cluster è rappresentato
     * utilizzando il metodo `toString(Data data)` del cluster.
     * 
     * @param data l'oggetto Data utilizzato per ottenere gli esempi associati agli indici.
     * @return una stringa che rappresenta gli esempi di tutti i cluster nel set.
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
     * Determina la coppia di cluster più simili utilizzando l'oggetto `distance` 
     * e fonde i due cluster in uno solo. Restituisce un nuovo `ClusterSet` con i cluster fusi.
     * 
     * @param distance l'oggetto per calcolare la distanza tra i cluster.
     * @param data l'oggetto che rappresenta il dataset.
     * @return una nuova istanza di `ClusterSet` con i cluster fusi.
     * @throws IllegalArgumentException se ci sono meno di due cluster nel set.
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
