package clustering;

import java.io.Serializable;

import data.Data;

/**
 * La classe Dendrogram rappresenta un dendrogramma, una struttura ad albero utilizzata per
 * modellare i cluster in un algoritmo di clustering gerarchico. Ogni livello del dendrogramma
 * contiene un oggetto ClusterSet che rappresenta i cluster a quel livello.
 */
public class Dendrogram implements Serializable {

    /** SerialVersionUID per garantire la compatibilità nella serializzazione. */
    private static final long serialVersionUID = 1L;

    /** Array di ClusterSet che modella il dendrogramma, con ogni livello contenente un ClusterSet. */
    public ClusterSet tree[];

    /**
     * Costruttore della classe Dendrogram. Inizializza il dendrogramma con un array di ClusterSet
     * della lunghezza specificata (profondità del dendrogramma).
     * 
     * @param depth la profondità del dendrogramma, ossia il numero di livelli.
     */
    public Dendrogram(int depth) {
        tree = new ClusterSet[depth];
    }

    /**
     * Imposta un ClusterSet a un livello specificato nel dendrogramma.
     * 
     * @param c il ClusterSet da impostare.
     * @param level il livello del dendrogramma dove impostare il ClusterSet.
     * @throws IllegalArgumentException se il livello è fuori dai limiti dell'array.
     */
    void setClusterSet(ClusterSet c, int level) {
        if (level >= 0 && level < tree.length) {
            tree[level] = c;
        } else {
            throw new IllegalArgumentException("livello fuori dai limiti del dendrogramma");
        }
    }

    /**
     * Restituisce il ClusterSet presente a un livello specificato nel dendrogramma.
     * 
     * @param level il livello da cui ottenere il ClusterSet.
     * @return il ClusterSet al livello specificato.
     * @throws IllegalArgumentException se il livello è fuori dai limiti dell'array.
     */
    ClusterSet getClusterSet(int level) {
        if (level >= 0 && level < tree.length) {
            return tree[level];
        } else {
            throw new IllegalArgumentException("livello fuori dai limiti del dendrogramma");
        }
    }

    /**
     * Restituisce la profondità del dendrogramma, ossia il numero di livelli contenuti.
     * 
     * @return la profondità del dendrogramma.
     */
    public int getDepth() {
        return tree.length;
    }

    /**
     * Restituisce una rappresentazione in forma di stringa del dendrogramma, con ogni livello
     * separato da una descrizione.
     * 
     * @return una stringa che rappresenta il dendrogramma.
     */
    public String toString() {
        String v = "";
        for (int i = 0; i < tree.length; i++) {
            v += ("level" + i + ":\n" + tree[i] + "\n");
        }
        return v;
    }

    /**
     * Restituisce una rappresentazione in forma di stringa del dendrogramma, utilizzando un oggetto
     * Data per ottenere la rappresentazione degli esempi contenuti in ogni cluster di ogni livello.
     * 
     * @param data l'oggetto Data che fornisce la rappresentazione degli esempi.
     * @return una stringa che rappresenta il dendrogramma, con i dati degli esempi.
     */
    String toString(Data data) {
        String v = "";
        for (int i = 0; i < tree.length; i++) {
            v += ("level" + i + ":\n" + tree[i].toString(data) + "\n");
        }
        return v;
    }
}
