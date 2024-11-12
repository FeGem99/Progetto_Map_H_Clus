package distance;

import clustering.Cluster;
import data.Data;
import data.Example;

/**
 * Implementazione dell'interfaccia {@link ClusterDistance} per il calcolo della distanza
 * tra due cluster utilizzando il metodo della Single Linkage.
 * 
 * La distanza tra due cluster è calcolata come la distanza minima tra tutti i possibili
 * accoppiamenti di esempi nei due cluster.
 */
public class SingleLinkDistance implements ClusterDistance {

    /**
     * Calcola la distanza tra due cluster utilizzando il metodo della Single Linkage.
     * 
     * La distanza tra due cluster è definita come la distanza minima tra qualsiasi coppia
     * di esempi appartenenti ai due cluster.
     * 
     * @param c1 Il primo cluster.
     * @param c2 Il secondo cluster.
     * @param d I dati utilizzati per ottenere gli esempi associati ai cluster.
     * @return La distanza minima tra i due cluster.
     */
    public double distance(Cluster c1, Cluster c2, Data d) {
        double min = Double.MAX_VALUE;  // Inizializza la distanza minima con il valore massimo possibile.

        // Per ogni esempio nel primo cluster, calcola la distanza con ogni esempio del secondo cluster.
        for (Integer id1 : c1) {
            Example e1 = d.getExample(id1);
            for (Integer id2 : c2) {
                // Calcola la distanza tra i due esempi.
                double distance = e1.distance(d.getExample(id2));
                
                // Se la distanza calcolata è inferiore alla minima trovata finora, aggiorna min.
                if (distance < min) {
                    min = distance;
                }
            }
        }
        
        // Restituisce la distanza minima trovata.
        return min;
    }
}
