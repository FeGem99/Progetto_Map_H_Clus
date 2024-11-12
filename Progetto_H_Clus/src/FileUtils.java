import java.io.File;

/**
 * Classe di utilità per operazioni sui file e sulle cartelle.
 * Contiene metodi per la gestione delle cartelle nel filesystem.
 */
public class FileUtils {

    /**
     * Crea una directory nel percorso specificato se non esiste già.
     * Se la cartella non esiste, viene creata, altrimenti non accade nulla.
     * 
     * @param directoryPath Il percorso della directory da creare.
     */
    @SuppressWarnings("unused")
    public static void createDirectoryIfNotExists(String directoryPath) {

        File directory = new File(directoryPath);
        // Verifica se la directory non esiste
        if (!directory.exists()) {
            directory.mkdirs();  // Crea la directory e le eventuali sottocartelle
            System.out.println("Cartella creata: " + directoryPath);
        }
    }
}
