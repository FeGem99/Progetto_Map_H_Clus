package database;

/**
 * Eccezione personalizzata che viene sollevata quando si verifica un errore nella connessione al database.
 */
public class DatabaseConnectionException extends Exception {

    /**
     * Costruttore della classe DatabaseConnectionException.
     * 
     * @param message il messaggio di errore associato all'eccezione.
     */
    public DatabaseConnectionException(String message) {
        super(message);
    }
}
