/**
 * Eccezione personalizzata che viene lanciata quando non vengono trovati dati.
 * Estende la classe {@link Exception}.
 */
public class NoDataException extends Exception {

    /**
     * Costruttore senza argomenti che crea un'eccezione con un messaggio predefinito.
     * Il messaggio predefinito è "Nessun dato trovato."
     */
    public NoDataException() {
        super("Nessun dato trovato.");
    }

    /**
     * Costruttore che permette di creare un'eccezione con un messaggio personalizzato.
     * 
     * @param message Il messaggio di errore che descrive l'eccezione.
     */
    public NoDataException(String message) {
        super(message);
    }

    /**
     * Costruttore che permette di creare un'eccezione con un messaggio personalizzato e una causa.
     * 
     * @param message Il messaggio di errore che descrive l'eccezione.
     * @param cause La causa (di tipo {@link Throwable}) che ha scatenato l'eccezione.
     */
    public NoDataException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruttore che permette di creare un'eccezione con una causa, senza messaggio personalizzato.
     * 
     * @param cause La causa (di tipo {@link Throwable}) che ha scatenato l'eccezione.
     */
    public NoDataException(Throwable cause) {
        super(cause);
    }
}
