import java.io.File;

/**
 * Classe utility per la gestione dei file e delle directory.
 * Contiene metodi per creare directory se non esistono già.
 */
public class FileUtils {
    
    /**
     * Crea una directory se non esiste già.
     * 
     * Se la directory specificata nel percorso non esiste, viene creata. 
     * Se la directory esiste già, il metodo non esegue alcuna operazione.
     * 
     * @param directoryPath Il percorso della directory da creare.
     *                      Se il percorso specificato è valido, la directory verrà creata.
     */
    @SuppressWarnings("unused")
    public static void createDirectoryIfNotExists(String directoryPath) {

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();  // Crea la directory, se necessario
            System.out.println("Cartella creata: " + directoryPath);
        }
    }
}
