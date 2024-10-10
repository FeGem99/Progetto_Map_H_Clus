package clustering;


@SuppressWarnings("serial")
/**
 * Eccezione controllata che indica un errore nella profondità del dendrogramma.
 * Viene lanciata se la profondità specificata è maggiore del numero di esempi nel dataset.
 */
public class InvalidDepthException extends Exception {

    /**
     * Costruttore che accetta un messaggio di errore.
     *
     * @param message Il messaggio di errore da visualizzare.
     */
    public InvalidDepthException(String message) {
        super(message);
    }
}
