package distance;

import clustering.Cluster;
import data.Data;

/**
 * Interfaccia che definisce il contratto per il calcolo della distanza tra due cluster.
 * Le implementazioni di questa interfaccia forniscono diversi metodi per calcolare la distanza
 * tra due oggetti di tipo {@link Cluster}.
 * 
 * @see Cluster
 * @see Data
 */
public interface ClusterDistance {

    /**
     * Calcola la distanza tra due cluster utilizzando un determinato metodo di distanza.
     * 
     * @param c1 Il primo cluster.
     * @param c2 Il secondo cluster.
     * @param d I dati che contengono gli esempi da utilizzare nel calcolo della distanza.
     * @return La distanza tra i due cluster.
     */
    double distance(Cluster c1, Cluster c2, Data d);
}
