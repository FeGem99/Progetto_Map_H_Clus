package distance;

import clustering.Cluster;
import data.Data;

/**
 * Interfaccia che definisce il contratto per il calcolo della distanza tra due cluster.
 * Diverse implementazioni di questa interfaccia possono utilizzare metodi differenti
 * per calcolare la distanza tra i cluster, ad esempio utilizzando il metodo dell'Average Linkage,
 * Single Linkage.
 * 
 * Le implementazioni di questa interfaccia dovrebbero definire il metodo
 * {@link #distance(Cluster, Cluster, Data)} per calcolare la distanza tra due cluster
 * basandosi sui dati forniti.
 */
public interface ClusterDistance {

    /**
     * Calcola la distanza tra due cluster dati i dati.
     * 
     * Questo metodo deve essere implementato dalle classi che definiscono il calcolo
     * della distanza tra i cluster. La distanza viene calcolata utilizzando i dati
     * forniti come input.
     * 
     * @param c1 Il primo cluster.
     * @param c2 Il secondo cluster.
     * @param d I dati utilizzati per ottenere gli esempi associati ai cluster.
     * @return La distanza tra i due cluster.
     */
    double distance(Cluster c1, Cluster c2, Data d);
}
