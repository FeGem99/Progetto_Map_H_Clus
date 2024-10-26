public class NoDataException extends Exception {
    
    // Costruttore senza argomenti
    public NoDataException() {
        super("Nessun dato trovato.");
    }

    // Costruttore con un messaggio personalizzato
    public NoDataException(String message) {
        super(message);
    }

    // Costruttore con messaggio e causa dell'eccezione
    public NoDataException(String message, Throwable cause) {
        super(message, cause);
    }

    // Costruttore con sola causa dell'eccezione
    public NoDataException(Throwable cause) {
        super(cause);
    }
}
