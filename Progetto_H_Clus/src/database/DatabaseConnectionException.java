package database;

/**
 * Classe di eccezione personalizzata che viene lanciata quando si verifica un errore
 * nella connessione al database.
 */
public class DatabaseConnectionException extends Exception {

    /**
     * Costruttore che accetta un messaggio di errore.
     * 
     * @param message il messaggio di errore da associare all'eccezione.
     */
    public DatabaseConnectionException(String message) {
        super(message);
    }
}
