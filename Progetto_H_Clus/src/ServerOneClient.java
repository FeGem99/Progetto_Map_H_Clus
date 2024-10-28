import java.io.*;
import java.net.Socket;

public class ServerOneClient implements Runnable {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ServerOneClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            // Inizializza i flussi di input e output
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            // Gestisce la comunicazione col client
            handleClient();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore di connessione con il client: " + e.getMessage());
        } finally {
            // Chiude il socket e i flussi
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                System.err.println("Errore nella chiusura delle risorse: " + e.getMessage());
            }
        }
    }

    private void handleClient() throws IOException, ClassNotFoundException {
        boolean exit = false;

        while (!exit) {
            int option = (Integer) in.readObject();

            switch (option) {
                case 0: // Carica il nome della tabella dal client
                    String tableName = (String) in.readObject();
                    boolean success = loadTableFromDatabase(tableName);
                    out.writeObject(success ? "OK" : "Tabella non trovata.");
                    break;
            
                case 1: // Apprende il dendrogramma dal database
                    int depth = (Integer) in.readObject();
                    int distanceType = (Integer) in.readObject();
                    String learnedDendrogram = learnDendrogramFromDatabase(depth, distanceType);
                    out.writeObject("OK");
                    out.writeObject(learnedDendrogram);
                    break;
            
                case 2: // Carica il dendrogramma da file
                    String fileName = (String) in.readObject();
                    String dendrogram = loadDendrogramFromFile(fileName);
                    if (dendrogram != null) {
                        out.writeObject("OK");
                        out.writeObject(dendrogram);
                    } else {
                        out.writeObject("Errore: File non trovato.");
                    }
                    break;
            
                case -1: // Fine della connessione
                    exit = true;
                    out.writeObject("Chiusura della connessione.");
                    break;
            
                default:
                    out.writeObject("Opzione non valida.");
                    break;
            }            
        }
    }

    private boolean loadTableFromDatabase(String tableName) {
        // Simulazione del caricamento della tabella
        return "exampletab".equals(tableName);
    }

    private String loadDendrogramFromFile(String fileName) {
        // Simulazione del caricamento del dendrogramma da file
        if ("example3.dat".equals(fileName)) {
            return "Dendrogramma caricato da " + fileName;
        }
        return null;
    }

    private String learnDendrogramFromDatabase(int depth, int distanceType) {
        // Simulazione dell'apprendimento del dendrogramma dal database
        return "Dendrogramma appreso con profondità " + depth + " e distanza " + (distanceType == 1 ? "single-link" : "average-link");
    }
}
