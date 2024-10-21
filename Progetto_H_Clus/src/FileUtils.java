import java.io.File;

public class FileUtils {
    @SuppressWarnings("unused")
    public static void createDirectoryIfNotExists(String directoryPath) {

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();  // Crea la directory, se necessario
            System.out.println("Cartella creata: " + directoryPath);
        }
    }
}
