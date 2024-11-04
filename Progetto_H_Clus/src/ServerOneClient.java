import java.io.*;
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
                Object requestObj = in.readObject();
                if (!(requestObj instanceof Integer)) {
                    out.writeObject("Errore: comando non riconosciuto.");
                    continue;
                }
                
                int request = (Integer) requestObj;
                if (request == -1) {
                    System.out.println("Client ha chiuso la connessione.");
                    break;
                }
                
                switch (request) {
                    case 0 -> loadData();
                    case 1 -> mineDendrogram();
                    case 2 -> loadDendrogramFromFile();
                    
                }
            }
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
        Object depthObj = in.readObject();
        Object dTypeObj = in.readObject();

        if (!(depthObj instanceof Integer) || !(dTypeObj instanceof Integer)) {
            out.writeObject("Errore: tipo dei dati non valido. Attesi Integer per profondità e tipo di distanza.");
            return;
        }

        int depth = (Integer) depthObj;
        int dType = (Integer) dTypeObj;

        try {
            ClusterDistance distance = switch (dType) {
                case 1 -> new SingleLinkDistance();
                case 2 -> new AverageLinkDistance();
                default -> throw new IllegalArgumentException("Tipo di distanza non valido");
            };

            Data data = new Data("exampletab");  // Ottieni i dati precedentemente caricati
            HierachicalClusterMiner clustering = new HierachicalClusterMiner(depth);
            clustering.mine(data, distance);

            out.writeObject("OK");
            out.writeObject(clustering.toString(data)); // Manda il dendrogramma al client

            String fileName = (String) in.readObject();
            saveDendrogram(clustering.toString(), fileName);
            out.writeObject("Dendrogramma salvato correttamente.");
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
