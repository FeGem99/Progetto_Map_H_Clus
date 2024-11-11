package database;

/**
 * Eccezione personalizzata che viene sollevata quando un numero mancante viene rilevato durante l'elaborazione dei dati.
 */
public class MissingNumberException extends Exception {

    /**
     * Costruttore della classe MissingNumberException.
     * 
     * @param message il messaggio di errore associato all'eccezione.
     */
    public MissingNumberException(String message) {
        super(message);
    }
}
