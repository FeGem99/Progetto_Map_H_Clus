package database;

/**
 * Eccezione personalizzata che viene sollevata quando un set è vuoto, ma si cerca di eseguire un'operazione su di esso.
 */
public class EmptySetException extends Exception {

    /**
     * Costruttore della classe EmptySetException.
     * 
     * @param message il messaggio di errore associato all'eccezione.
     */
    public EmptySetException(String message) {
        super(message);
    }
}
