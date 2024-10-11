package distance;

    @SuppressWarnings("serial")
/**
 * Eccezione controllata che indica un errore nelle dimensioni degli esempi.
 * Viene lanciata se si tenta di calcolare la distanza tra due esempi di dimensioni diverse.
 */
public class InvalidSizeException extends Exception {

    /**
     * Costruttore che accetta un messaggio di errore.
     *
     * @param message Il messaggio di errore da visualizzare.
     */
    public InvalidSizeException(String message) {
        super(message);
    }
}

