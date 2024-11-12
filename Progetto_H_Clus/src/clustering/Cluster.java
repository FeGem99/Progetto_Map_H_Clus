package clustering;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import data.Data;

/**
 * La classe Cluster rappresenta un insieme di dati raggruppati insieme in un cluster.
 * Essa gestisce una collezione di indici di campioni (Integer) e fornisce metodi
 * per manipolare e ottenere informazioni sul cluster.
 */
public class Cluster implements Iterable<Integer>, Cloneable, Serializable {

    /**
     * Un insieme di indici di dati raggruppati nel cluster. 
     * Viene utilizzato un TreeSet per mantenere l'ordinamento naturale degli indici.
     */
    private Set<Integer> clusteredData = new TreeSet<>();

    /**
     * Restituisce un iteratore sugli indici dei dati nel cluster.
     * 
     * @return un iteratore che scorre gli indici dei dati nel cluster.
     */
    @Override
    public Iterator<Integer> iterator() {
        return clusteredData.iterator();
    }

    /**
     * Aggiunge l'indice di un campione al cluster.
     * 
     * @param id l'indice del campione da aggiungere al cluster.
     */
    void addData(int id) {
        clusteredData.add(id);
    }

    /**
     * Restituisce la dimensione del cluster, ovvero il numero di dati che lo compongono.
     * 
     * @return la dimensione del cluster.
     */
    public int getSize() {
        return clusteredData.size();
    }

    /**
     * Crea una copia del cluster. La copia contiene gli stessi indici di dati del cluster originale.
     * 
     * @return una nuova istanza di Cluster che è una copia di quello corrente.
     */
    public Object clone() {
        Cluster copyC = new Cluster();
        copyC.clusteredData.addAll(this.clusteredData);
        return copyC;
    }

    /**
     * Unisce il cluster corrente con un altro cluster.
     * 
     * @param c il cluster da unire con il cluster corrente.
     * @return un nuovo cluster che contiene gli indici di entrambi i cluster.
     */
    Cluster mergeCluster(Cluster c) {
        Cluster newC = new Cluster();
        newC.clusteredData.addAll(this.clusteredData);
        newC.clusteredData.addAll(c.clusteredData);
        return newC;
    }

    /**
     * Restituisce una rappresentazione in formato stringa del cluster.
     * Gli indici sono separati da virgole.
     * 
     * @return una stringa che rappresenta gli indici nel cluster.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Integer> iterator = clusteredData.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * Restituisce una rappresentazione in formato stringa del cluster, 
     * ma con i dati associati agli indici. Ogni elemento viene rappresentato 
     * come <dato>, dove "dato" è l'esempio associato all'indice.
     * 
     * @param data l'oggetto Data utilizzato per ottenere gli esempi associati agli indici.
     * @return una stringa che rappresenta gli esempi nel cluster.
     */
    String toString(Data data) {
        StringBuilder sb = new StringBuilder();
        Iterator<Integer> iterator = clusteredData.iterator();
        while (iterator.hasNext()) {
            sb.append("<").append(data.getExample(iterator.next())).append(">");
        }
        return sb.toString();
    }
}
