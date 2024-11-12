package database;

/**
 * Eccezione lanciata quando un set di dati è vuoto.
 */
public class EmptySetException extends Exception {

    /**
     * Costruttore per l'eccezione EmptySetException.
     * 
     * @param message Il messaggio di errore che descrive la causa dell'eccezione.
     */
    public EmptySetException(String message) {
        super(message);
    }
}
