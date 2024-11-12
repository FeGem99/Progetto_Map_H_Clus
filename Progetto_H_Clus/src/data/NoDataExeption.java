package data;

/**
 * Classe di eccezione personalizzata che viene lanciata quando non ci sono dati disponibili,
 * come nel caso in cui una tabella nel database sia vuota.
 */
public class NoDataExeption extends Exception {

    /**
     * Costruttore che accetta un messaggio di errore.
     * 
     * @param message il messaggio di errore da associare all'eccezione.
     */
    public NoDataExeption(String message) {
        super(message);
    }
}
