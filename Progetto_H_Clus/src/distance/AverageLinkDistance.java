package distance;

import clustering.Cluster;
import data.Data;
import data.Example;

/**
 * Implementazione dell'interfaccia {@link ClusterDistance} che calcola la distanza tra due cluster
 * utilizzando il metodo di distanza "Average Linkage". La distanza tra i cluster è calcolata come
 * la media delle distanze tra tutti i punti nei due cluster.
 */
public class AverageLinkDistance implements ClusterDistance {

    /**
     * Calcola la distanza tra due cluster utilizzando il metodo di distanza "Average Linkage".
     * La distanza media tra tutti i punti dei due cluster è calcolata come la somma delle distanze
     * tra ogni coppia di esempi dei cluster divisa per il numero totale di coppie.
     * 
     * @param c1 Il primo cluster.
     * @param c2 Il secondo cluster.
     * @param d I dati da cui vengono estratti gli esempi.
     * @return La distanza media tra i due cluster.
     */
    @Override
    public double distance(Cluster c1, Cluster c2, Data d) {
        double totalDistance = 0.0;
        int numDistance = 0;

        // Per ogni esempio nel primo cluster
        for (Integer id1 : c1) {
            Example e1 = d.getExample(id1);

            // Per ogni esempio nel secondo cluster
            for (Integer id2 : c2) {
                Example e2 = d.getExample(id2);
                // Aggiungi la distanza tra le due istanze
                totalDistance += e1.distance(e2);
                numDistance++;
            }
        }
        // Calcola e restituisce la distanza media
        return totalDistance / numDistance;
    }
}
