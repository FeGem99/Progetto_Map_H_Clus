package data;

/**
 * Eccezione personalizzata che viene sollevata quando si verifica un errore legato all'assenza di dati.
 */
public class NoDataExeption extends Exception {

    /**
     * Costruttore della classe NoDataExeption.
     * 
     * @param message il messaggio di errore associato all'eccezione.
     */
    public NoDataExeption(String message) {
        super(message);
    }
}
