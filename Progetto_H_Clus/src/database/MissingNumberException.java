package database;

/**
 * Eccezione lanciata quando manca un numero richiesto nei dati.
 */
public class MissingNumberException extends Exception {

    /**
     * Costruttore per l'eccezione MissingNumberException.
     * 
     * @param message Il messaggio di errore che descrive la causa dell'eccezione.
     */
    public MissingNumberException(String message) {
        super(message);
    }
}
