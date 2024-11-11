package clustering;

/**
 * Eccezione controllata che viene sollevata quando la dimensione di un oggetto o un parametro
 * non soddisfa i requisiti previsti.
 */
public class InvalidSizeException extends Exception {

    /**
     * Costruttore della classe InvalidSizeException che inizializza l'eccezione con un messaggio personalizzato.
     * 
     * @param message il messaggio che descrive il motivo dell'eccezione.
     */
    public InvalidSizeException(String message) {
        super(message);
    }
}
