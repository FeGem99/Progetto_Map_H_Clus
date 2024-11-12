package data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Rappresenta un singolo esempio nel dataset, che è composto da una lista di valori numerici.
 * La classe implementa l'interfaccia {@link Iterable} per permettere l'iterazione sui valori dell'esempio.
 */
public class Example implements Iterable<Double> {
    public List<Double> example;

    /**
     * Costruttore che inizializza l'esempio con una lista vuota.
     */
    public Example() {
        example = new LinkedList<>();
    }

    /**
     * Restituisce un iteratore per l'esempio, che permette di iterare sui valori.
     * 
     * @return un iteratore sui valori dell'esempio.
     */
    @Override
    public Iterator<Double> iterator() {
        return example.iterator();
    }

    /**
     * Aggiunge un valore all'esempio.
     * 
     * @param v il valore da aggiungere.
     */
    public void add(Double v) {
        example.add(v);
    }

    /**
     * Restituisce il valore dell'esempio all'indice specificato.
     * 
     * @param index l'indice del valore da ottenere.
     * @return il valore all'indice specificato.
     */
    Double get(int index) {
        return example.get(index);
    }

    /**
     * Calcola la distanza quadratica tra questo esempio e un altro esempio.
     * La distanza è definita come la somma dei quadrati delle differenze
     * tra i valori corrispondenti degli esempi.
     * 
     * @param newE l'altro esempio con cui calcolare la distanza.
     * @return la distanza quadratica tra i due esempi.
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
     * La rappresentazione è una lista di valori separati da virgole.
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
