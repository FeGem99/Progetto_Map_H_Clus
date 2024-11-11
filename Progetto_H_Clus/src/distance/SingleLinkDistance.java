package distance;

import clustering.Cluster;
import data.Data;
import data.Example;

/**
 * Classe che implementa l'interfaccia {@link ClusterDistance} utilizzando il metodo di distanza 
 * "Single Link" per calcolare la distanza tra due cluster.
 * 
 * Il metodo "Single Link" definisce la distanza tra due cluster come la distanza minima 
 * tra tutti i possibili accoppiamenti di esempi appartenenti ai due cluster.
 * 
 * @see ClusterDistance
 * @see Cluster
 * @see Data
 * @see Example
 */
public class SingleLinkDistance implements ClusterDistance {

    /**
     * Calcola la distanza tra due cluster utilizzando il metodo "Single Link".
     * 
     * @param c1 Il primo cluster.
     * @param c2 Il secondo cluster.
     * @param d I dati che contengono gli esempi da utilizzare nel calcolo della distanza.
     * @return La distanza minima tra i due cluster, calcolata come la distanza tra il 
     *         più vicino accoppiamento di esempi tra i cluster.
     */
    public double distance(Cluster c1, Cluster c2, Data d) {
        
        double min = Double.MAX_VALUE;
        
        // Per ogni esempio nel cluster c1
        for (Integer id1 : c1) {
            Example e1 = d.getExample(id1);
            
            // Per ogni esempio nel cluster c2
            for (Integer id2 : c2) {
                // Calcola la distanza tra gli esempi
                double distance = e1.distance(d.getExample(id2));
                
                // Se la distanza è inferiore alla distanza minima attuale, aggiorna il valore
                if (distance < min) {
                    min = distance;
                }
            }
        }
        return min; // Restituisce la distanza minima
    }
}
