package distance;

import clustering.Cluster;
import data.Data;
import data.Example;

/**
 * Implementazione dell'algoritmo di distanza con il metodo dell'Average Linkage
 * per il calcolo della distanza tra due cluster.
 * 
 * In questo metodo, la distanza tra due cluster viene calcolata come la
 * distanza media tra tutte le coppie di esempi appartenenti ai due cluster.
 */
public class AverageLinkDistance implements ClusterDistance {

    /**
     * Calcola la distanza media tra due cluster utilizzando il metodo Average Linkage.
     * 
     * @param c1 Il primo cluster.
     * @param c2 Il secondo cluster.
     * @param d I dati utilizzati per ottenere gli esempi associati ai cluster.
     * @return La distanza media tra i due cluster.
     */
    @Override
    public double distance(Cluster c1, Cluster c2, Data d) {
        double totalDistance = 0.0;
        int numDistance = 0;

        // Per ogni esempio nel primo cluster
        for (Integer id1: c1) {
            Example e1 = d.getExample(id1);

            // Per ogni esempio nel secondo cluster
            for(Integer id2 : c2){
                Example e2 = d.getExample(id2);
                totalDistance += e1.distance(e2);  // Somma delle distanze
                numDistance++;  // Conta il numero delle distanze calcolate
            }
        }

        // Restituisce la distanza media
        return totalDistance / numDistance;
    }
}
