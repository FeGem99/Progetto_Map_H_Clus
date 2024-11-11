package clustering;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import data.Data;

/**
 * La classe Cluster rappresenta un cluster di dati, identificato da un insieme di ID di esempio.
 * Implementa l'interfaccia Iterable per permettere l'iterazione sui dati del cluster.
 * Implementa l'interfaccia Serializable per consentire la serializzazione del cluster.
 */
public class Cluster implements Iterable<Integer>, Cloneable, Serializable {
    
    /** 
     * L'attributo clusteredData contiene un insieme di ID di esempio che appartengono al cluster.
     */
    private Set<Integer> clusteredData = new TreeSet<>();
    
    /**
     * Restituisce un iteratore per attraversare gli ID degli esempi contenuti nel cluster.
     * 
     * @return un iteratore su un insieme di ID.
     */
    @Override
    public Iterator<Integer> iterator() {
        return clusteredData.iterator();
    }
    
    /**
     * Aggiunge un ID di esempio al cluster.
     * 
     * @param id l'ID dell'esempio da aggiungere al cluster.
     */
    void addData(int id) {
        clusteredData.add(id);        
    }

    /**
     * Restituisce la dimensione del cluster, ovvero il numero di esempi che contiene.
     * 
     * @return la dimensione del cluster.
     */
    public int getSize() {
        return clusteredData.size();
    }

    /**
     * Crea una copia profonda del cluster corrente.
     * 
     * @return un nuovo oggetto Cluster che è una copia del cluster corrente.
     */
    public Object clone() {
        Cluster copyC = new Cluster();
        copyC.clusteredData.addAll(this.clusteredData);
        return copyC;
    }

    /**
     * Unisce il cluster corrente con un altro cluster, creandone un nuovo che contiene
     * gli ID di entrambi.
     * 
     * @param c il cluster da unire con il cluster corrente.
     * @return un nuovo cluster contenente gli ID di entrambi i cluster.
     */
    Cluster mergeCluster(Cluster c) {
        Cluster newC = new Cluster();
        newC.clusteredData.addAll(this.clusteredData);
        newC.clusteredData.addAll(c.clusteredData);
        return newC;
    }

    /**
     * Restituisce una rappresentazione in forma di stringa degli ID contenuti nel cluster.
     * 
     * @return una stringa contenente gli ID del cluster.
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
     * Restituisce una rappresentazione in forma di stringa degli esempi contenuti nel cluster.
     * Gli ID degli esempi sono sostituiti dalla loro rappresentazione testuale ottenuta tramite
     * l'oggetto Data.
     * 
     * @param data l'oggetto Data che permette di ottenere la rappresentazione testuale di un esempio.
     * @return una stringa contenente la rappresentazione degli esempi nel cluster.
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
