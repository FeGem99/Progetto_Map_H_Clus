package clustering;

/**
 * Eccezione personalizzata che viene lanciata quando la dimensione di un oggetto o parametro
 * non è valida o non soddisfa i criteri richiesti.
 */
public class InvalidSizeException extends Exception {

    /**
     * Costruttore che accetta un messaggio di errore.
     * 
     * @param message il messaggio che descrive il motivo dell'eccezione.
     */
   public InvalidSizeException(String message) {
      super(message);
   }
}
