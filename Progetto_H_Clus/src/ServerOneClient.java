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
        } catch (InvalidDepthException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    private void handleClient() throws IOException, ClassNotFoundException, InvalidDepthException {
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
                
                    // Invia il risultato al client
                    if (learnedDendrogram.startsWith("Errore")) {
                        out.writeObject(learnedDendrogram);  // Invia un messaggio di errore
                    } else {
                        out.writeObject("OK");
                        out.writeObject(learnedDendrogram);  // Invia il dendrogramma appreso
                    }
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

    private String learnDendrogramFromDatabase(int depth, int distanceType) throws IOException, InvalidDepthException {
    // Crea l'oggetto Data per caricare i dati dal database
    Data data;
    try {
        data = new Data("exampletab");  // Nome della tabella, potrebbe essere dinamico
    } catch (Exception e) {
        return "Errore nella creazione dell'oggetto Data: " + e.getMessage();
    }

    // Verifica che la profondità sia valida
    if (depth < 1 || depth > data.getNumberOfExample()) {
        return "Profondità del dendrogramma non valida.";
    }

    // Crea il ClusterDistance in base al tipo di distanza
    ClusterDistance distance = null;
    switch (distanceType) {
        case 1:
            distance = new SingleLinkDistance();
            break;
        case 2:
            distance = new AverageLinkDistance();
            break;
        default:
            return "Tipo di distanza non valido.";
    }

    // Crea l'oggetto HierachicalClusterMiner e esegui il clustering
    HierachicalClusterMiner clustering = new HierachicalClusterMiner(depth);
    clustering.mine(data, distance);

    // Restituisce il dendrogramma come stringa per essere visualizzato dal client
    return clustering.toString(data);
}

}
