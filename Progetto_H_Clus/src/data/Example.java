package data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Rappresenta un esempio all'interno di un dataset, contenente una lista di valori numerici.
 * La classe implementa l'interfaccia Iterable per permettere l'iterazione sugli elementi dell'esempio.
 */
public class Example implements Iterable<Double> {

    /** Lista che contiene i valori numerici dell'esempio. */
    public List<Double> example;

    /**
     * Costruttore della classe Example. Inizializza la lista degli esempi come una LinkedList vuota.
     */
    public Example() {
        example = new LinkedList<>();
    }

    /**
     * Restituisce un iteratore per iterare sui valori dell'esempio.
     * 
     * @return un iteratore sui valori numerici dell'esempio.
     */
    public Iterator<Double> iterator() {
        return example.iterator();
    }

    /**
     * Aggiunge un nuovo valore all'esempio.
     * 
     * @param v il valore da aggiungere all'esempio.
     */
    public void add(Double v) {
        example.add(v);
    }

    /**
     * Restituisce il valore dell'esempio alla posizione indicata dall'indice.
     * 
     * @param index l'indice del valore da recuperare.
     * @return il valore all'indice specificato.
     */
    Double get(int index) {
        return example.get(index);
    }

    /**
     * Calcola la distanza euclidea tra l'esempio corrente e un altro esempio.
     * 
     * @param newE l'esempio con cui calcolare la distanza.
     * @return la distanza euclidea tra i due esempi.
     */
    public double distance(Example newE) {
        Double sum = 0.0;
        Iterator<Double> thisIter = this.iterator();
        Iterator<Double> newIter = newE.iterator();

        while (thisIter.hasNext() && newIter.hasNext()) {
            Double diff = thisIter.next() - newIter.next();
            sum += diff * diff;
        }
        return sum;
    }

    /**
     * Restituisce una rappresentazione in formato stringa dell'esempio.
     * La stringa rappresenta la lista di valori numerici contenuti nell'esempio.
     * 
     * @return una stringa che rappresenta l'esempio.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < example.size(); i++) {
            sb.append(example.get(i));
            if (i < example.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
