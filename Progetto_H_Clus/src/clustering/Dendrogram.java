package clustering;

import java.io.Serializable;
import data.Data;

/**
 * La classe Dendrogram rappresenta un dendrogramma che modella una gerarchia di cluster.
 * Essa contiene un array di oggetti `ClusterSet`, ciascuno dei quali rappresenta un livello
 * del dendrogramma durante il processo di clustering gerarchico.
 */
public class Dendrogram implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Un array che memorizza i cluster per ogni livello del dendrogramma.
     * Ogni livello rappresenta una fase del clustering gerarchico.
     */
    public ClusterSet tree[];

    /**
     * Costruisce un nuovo dendrogramma con una profondità specificata.
     * 
     * @param depth la profondità del dendrogramma, ovvero il numero di livelli.
     */
    public Dendrogram(int depth) {
        tree = new ClusterSet[depth];
    }

    /**
     * Imposta un `ClusterSet` per un livello specifico del dendrogramma.
     * 
     * @param c il `ClusterSet` da impostare.
     * @param level il livello del dendrogramma in cui impostare il `ClusterSet`.
     * @throws IllegalArgumentException se il livello è fuori dai limiti del dendrogramma.
     */
    void setClusterSet(ClusterSet c, int level) {
        if (level >= 0 && level < tree.length) {
            tree[level] = c;
        } else {
            throw new IllegalArgumentException("livello fuori dai limiti del dendrogramma");
        }
    }

    /**
     * Restituisce il `ClusterSet` associato a un livello specificato del dendrogramma.
     * 
     * @param level il livello del dendrogramma da cui ottenere il `ClusterSet`.
     * @return il `ClusterSet` associato al livello.
     * @throws IllegalArgumentException se il livello è fuori dai limiti del dendrogramma.
     */
    ClusterSet getClusterSet(int level) {
        if (level >= 0 && level < tree.length) {
            return tree[level];
        } else {
            throw new IllegalArgumentException("livello fuori dai limiti del dendrogramma");
        }
    }

    /**
     * Restituisce la profondità del dendrogramma, cioè il numero di livelli.
     * 
     * @return la profondità del dendrogramma.
     */
    public int getDepth() {
        return tree.length;
    }

    /**
     * Restituisce una rappresentazione in formato stringa del dendrogramma.
     * Ogni livello viene rappresentato con il proprio indice e il `ClusterSet` associato.
     * 
     * @return una stringa che rappresenta l'intero dendrogramma.
     */
    public String toString() {
        String v = "";
        for (int i = 0; i < tree.length; i++)
            v += ("level" + i + ":\n" + tree[i] + "\n");
        return v;
    }

    /**
     * Restituisce una rappresentazione in formato stringa del dendrogramma, 
     * con i dati associati ai cluster di ogni livello.
     * Ogni livello è rappresentato utilizzando il metodo `toString(Data data)` del `ClusterSet`.
     * 
     * @param data l'oggetto Data utilizzato per ottenere gli esempi associati ai cluster.
     * @return una stringa che rappresenta l'intero dendrogramma con i dati.
     */
    String toString(Data data) {
        String v = "";
        for (int i = 0; i < tree.length; i++)
            v += ("level" + i + ":\n" + tree[i].toString(data) + "\n");
        return v;
    }
}
