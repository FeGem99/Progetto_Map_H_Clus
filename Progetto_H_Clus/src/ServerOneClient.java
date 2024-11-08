import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import clustering.HierachicalClusterMiner;
import clustering.InvalidDepthException;
import data.Data;
import distance.AverageLinkDistance;
import distance.ClusterDistance;
import distance.SingleLinkDistance;

public class ServerOneClient implements Runnable {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private static final String DIRECTORY_PATH = "Saved_Object";

    public ServerOneClient(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
    }

    @Override
public void run() {
    try {
        while (true) {
            // Log per tracciare i dati ricevuti
            System.out.println("In attesa di un comando dal client...");

            Object requestObj = in.readObject();
            System.out.println("Ricevuto comando: " + requestObj);

            // Verifica se l'input è del tipo Integer, altrimenti continua con il messaggio di errore
            if (!(requestObj instanceof Integer)) {
                out.writeObject("Errore: comando non riconosciuto. Assicurati di inviare un comando numerico.");
                continue;
            }
            
            int request = (Integer) requestObj;
            
            // Condizione per terminare la connessione
            if (request == -1) {
                System.out.println("Client ha chiuso la connessione.");
                break;
            }
            
            // Esegui i vari comandi
            switch (request) {
                case 0 -> loadData();
                case 1 -> mineDendrogram();
                case 2 -> loadDendrogramFromFile();
                default -> {
                    // Comando non valido: log dettagliato
                    System.out.println("Ricevuto comando non valido: " + request);
                    out.writeObject("Errore: comando non riconosciuto.");
                }
            }
        }
    } catch (EOFException eof) {
        System.out.println("Il client ha chiuso la connessione improvvisamente.");
    } catch (Exception e) {
        System.out.println("Errore durante l'elaborazione della richiesta del client: " + e.getMessage());
    } finally {
        closeConnection();
    }
}


    private void loadData() throws IOException, ClassNotFoundException {
        String tableName = (String) in.readObject();
        try {
            Data data = new Data(tableName);
            out.writeObject("OK");
            System.out.println("Oggetto Data creato con successo per la tabella: " + tableName);
        } catch (Exception e) {
            out.writeObject("Errore durante il caricamento dei dati: " + e.getMessage());
        }
    }

    private void mineDendrogram() throws IOException, ClassNotFoundException, InvalidDepthException {
        Object tableNameObj = in.readObject();
        Object depthObj = in.readObject();
        Object dTypeObj = in.readObject();
    
        if (!(tableNameObj instanceof String) || !(depthObj instanceof Integer) || !(dTypeObj instanceof Integer)) {
            out.writeObject("Errore: tipo dei dati non valido. Attesi String per il nome della tabella, Integer per profondità e tipo di distanza.");
            return;
        }
    
        String tableName = (String) tableNameObj;
        int depth = (Integer) depthObj;
        int dType = (Integer) dTypeObj;
    
        try {
            ClusterDistance distance = switch (dType) {
                case 1 -> new SingleLinkDistance();
                case 2 -> new AverageLinkDistance();
                default -> throw new IllegalArgumentException("Tipo di distanza non valido");
            };
    
            // Creazione dinamica dell'oggetto Data usando il nome della tabella fornito
            Data data = new Data(tableName); 
            HierachicalClusterMiner clustering = new HierachicalClusterMiner(depth);
            clustering.mine(data, distance);
    
            out.writeObject("OK");
            out.writeObject(clustering.toString(data)); // Manda il dendrogramma al client
    
            // Ricezione del nome del file come stringa
            Object fileNameObj = in.readObject();
            if (!(fileNameObj instanceof String)) {
                out.writeObject("Errore: il nome del file deve essere una stringa.");
                return;
            }
    
            String fileName = (String) fileNameObj;
            System.out.println("Nome del file inserito: " + fileName);
    
            saveDendrogram(clustering.toString(), fileName);
            out.writeObject("Dendrogramma salvato correttamente.");
            
            System.out.println("Dendrogramma salvato, operazione completata.");
    
        } catch (Exception e) {
            out.writeObject("Errore durante l'apprendimento del dendrogramma: " + e.getMessage());
        }
    }
    
    
    
    
    

    private void loadDendrogramFromFile() throws IOException, ClassNotFoundException {
        String fileName = (String) in.readObject();
        String fullPath = DIRECTORY_PATH + "/" + fileName;

        try {
            String dendrogramData = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(fullPath)));
            out.writeObject("OK");
            out.writeObject(dendrogramData);
            System.out.println("Dendrogramma caricato e inviato al client.");
        } catch (IOException e) {
            out.writeObject("Errore durante il caricamento del file: " + e.getMessage());
        }
    }

    private void saveDendrogram(String dendrogramString, String fileName) throws IOException {
        if (!fileName.endsWith(".dat")) {
            fileName += ".dat";
        }
        String fullPath = DIRECTORY_PATH + "/" + fileName;
        java.nio.file.Files.write(java.nio.file.Paths.get(fullPath), dendrogramString.getBytes());
        System.out.println("Dendrogramma salvato in: " + fullPath);
    }

    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (clientSocket != null) clientSocket.close();
            System.out.println("Connessione con il client chiusa.");
        } catch (IOException e) {
            System.out.println("Errore durante la chiusura della connessione: " + e.getMessage());
        }
    }
}
