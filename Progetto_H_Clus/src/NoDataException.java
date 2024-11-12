/**
 * Classe che rappresenta un'eccezione lanciata quando non sono presenti dati.
 * Estende la classe `Exception` per gestire errori specifici relativi alla mancanza di dati.
 */
public class NoDataException extends Exception {

    /**
     * Costruttore senza argomenti che fornisce un messaggio predefinito.
     * Il messaggio predefinito è "Nessun dato trovato."
     */
    public NoDataException() {
        super("Nessun dato trovato.");
    }

    /**
     * Costruttore con un messaggio personalizzato.
     * 
     * @param message Il messaggio di errore che descrive la causa dell'eccezione.
     */
    public NoDataException(String message) {
        super(message);
    }

    /**
     * Costruttore con un messaggio personalizzato e una causa.
     * 
     * @param message Il messaggio di errore che descrive la causa dell'eccezione.
     * @param cause La causa dell'eccezione, che può essere un'altra eccezione.
     */
    public NoDataException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruttore con una causa dell'eccezione.
     * 
     * @param cause La causa dell'eccezione, che può essere un'altra eccezione.
     */
    public NoDataException(Throwable cause) {
        super(cause);
    }
}
